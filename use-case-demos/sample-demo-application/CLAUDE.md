# CLAUDE.md

Guidance for Claude Code (claude.ai/code) when working on the sample demo application.

## What this is

A Third-Party Provider (TPP) demonstrator for the WSO2 Open Banking / Financial Services
accelerator. It is a **single self-contained WAR** (`api-ob-demo-1.0.0.war`) deployed into WSO2
Identity Server's `repository/deployment/server/webapps/`, serving both:

- a React single page application (the customer-facing UI), and
- a JAX-RS backend under `/init/*` (CXF, `ApiController`) that performs the OAuth and Open
  Banking API calls with mTLS and signed JWTs.

Docker files for a throwaway sandbox live in `docker-files/` and `build.sh`, but the app is
normally built and dropped into an existing IS + APIM pair.

## Build

```bash
cd demo-application
mvn clean package -DskipTests     # add -o to build offline
```

- **Maven builds the frontend too** (`frontend-maven-plugin` → pnpm + `vite build`), then copies
  `frontend/dist` into `src/main/webapp`. So **make UI changes in `frontend/src/`, never in
  `src/main/webapp/assets/*.js`** — those are build output. Old hashed bundles accumulate there
  and only the one named in `src/main/webapp/index.html` is loaded; the rest are dead files.
- **`src/main/webapp/configurations/config.json` is build output too**, copied from
  `frontend/public/configurations/config.json` by way of `frontend/dist`. Edit the one under
  `frontend/public`. Editing the `src/main/webapp` copy appears to work and is then silently
  reverted by the next build, which is easy to miss because both paths are tracked in git.
- **SpotBugs runs at `compile` with `threshold=Low, effort=Max` and fails the build.** Expect it
  to reject storing an externally mutable object (`EI_EXPOSE_REP2` — copy defensively), a
  `Serializable` without `serialVersionUID`, and predictable randomness (use `SecureRandom`).
  Checkstyle is present but commented out.
- Java 8/11 source level via the WSO2 parent POM; no records, no `var`.
- There are no unit tests. Verify by building, deploying and running the flows.

## Deploying into WSO2 IS

```bash
cd <IS_HOME>/repository/deployment/server/webapps
rm -rf api-ob-demo-1.0.0 api-ob-demo-1.0.0.war   # remove BOTH first
cp <repo>/demo-application/target/api-ob-demo-1.0.0.war .
```

- Keep the WAR name exactly `api-ob-demo-1.0.0.war`: the context path is baked into the built
  frontend and into `application.properties`.
- **Never copy a new WAR while the old exploded directory is still present.** Carbon reloads the
  context, each reload is a new classloader, and anything in a static field is silently wiped
  between two HTTP requests — which breaks the OAuth round trip in ways that look like an
  Identity Server bug.
- App URL: `https://<is-host>:<is-port>/api-ob-demo-1.0.0` (no trailing slash).

## Authorization flow

The app uses **Rich Authorization Requests** (RFC 9396). There is no consent initiation call:

```
POST /oauth2/par        signed request object + authorization_details + PKCE + private_key_jwt
GET  /oauth2/authorize  client_id + request_uri  →  customer authorizes
     callback           ?response=<JARM JWT>     →  code is inside the JWT
POST /oauth2/token      code + code_verifier + client_assertion
GET|POST <gateway>/open-banking/v1.0/{aisp,pisp}/...
```

- `response_mode=jwt` (JARM), so the callback carries a signed `response` JWT rather than
  `?code=`. `AuthService.resolveAuthorizationCode` decodes it.
- The **consent id is a claim on the user access token** (`oauth.consent.id.claim`, default
  `consent_id`), decoded by `JwtDecoder`. The payment submission echoes it back as
  `Data.ConsentId`. This requires `append_consent_id_to_access_token=true` on the server.
- A payment's `Initiation` is built once, stored, authorized, and then submitted **as the very
  same object**, so a submission cannot drift from what was consented to.
- Request/response shapes follow the `reference-implementation-non-regulated-ob` repo: its
  `apis/*/`, `rar-schemas/*.json` and the Postman collection are the contract. RAR types must be
  registered in IS and the client authorized for them (`rar-schemas/scripts/`) or PAR will fail.
- The pre-initiated flow (`/account-access-consents`, `openbanking_intent_id`,
  `client_credentials`, `accounts`/`payments` scopes) was removed deliberately — do not
  reintroduce it alongside RAR.

The PAR body carries exactly five parameters - `request`, `client_assertion_type`, `client_id`,
`client_assertion`, `authorization_details` - matching the reference implementation's Postman
collection, which is the contract here. Everything else describing the authorization
(`response_type`, `response_mode`, `redirect_uri`, `scope`, `nonce`, the PKCE code challenge,
`prompt`) is a claim in the signed request object, so it is covered by the signature. Do not
re-add a form parameter for something the request object already carries without a reason.

The token request follows the collection too: `grant_type`, `client_assertion_type`,
`client_assertion`, `redirect_uri`, `client_id`, `code`, `code_verifier`, all URL-encoded, and
**no `scope`** - the access was described by `authorization_details` at the pushed request, so
asking again is meaningless. The API calls send only `Authorization` and `Accept`; the
`x-fapi-financial-id` header this used to add is a UK Open Banking header that neither the
collection nor `account-info-openapi.yaml` knows about.

`oauth.prompt=login` makes the Identity Server re-authenticate the customer on the consent
redirect instead of reusing single sign on. It is the **one** parameter that must be pushed as a
PAR form parameter rather than being a claim in the request object, and that is not a style
choice: `OAuthParRequestWrapper` re-exposes pushed form parameters as request parameters at
`/authorize`, whereas the request object is only mined for a known set of parameters that does
not include `prompt`. Sent as a claim it is silently ignored - the session is reused and no login
appears, verified on IS 7.3. As a pushed parameter it works, also verified. The reference
collection uses `prompt` nowhere, so it offers no shape to follow here. The SPA sign in is
deliberately left on single sign on, so one add-account run asks for credentials once rather than
twice; `prompt` in `frontend/src/authConfig.ts` is what would change that.

Config lives in `src/main/resources/application.properties`; `ConfigLoader` is the only reader.
`obtransport.pem` / `obtransport.key` (mTLS) and `obsigning.key` (JWT signing) are classpath
resources — **if you swap in local test certificates, do not commit them.**

## Developer console

A slide-out panel on the right of every page shows each call of the flow: what was sent, what
came back, and the claims of the JWTs involved. It exists because a browser network panel is
useless here - the browser only ever sees this application's own `/init/*` endpoints, and the
whole Open Banking flow happens on the server side.

```
devconsole/FlowLog          the session's captured steps, capped at 60
devconsole/FlowRecorder     where code writes what it did; a ThreadLocal log
devconsole/FlowRecorderFilter  binds the session's log to the thread for /init/*
devconsole/FlowFormatter    renders bodies; unpacks form bodies and decodes JWTs in them
GET|DELETE /init/dev-console   read and clear the log
frontend/src/components/dev-console/  the panel, polled only while it is open
```

- **Capture happens in `HttpConnection`**, so every outbound call is recorded at one place with
  its real status - including calls that fail, and calls that never got a status at all.
  `HttpTlsClient` only adds `.withLabel(...)` to name each step.
- **The two browser hops are recorded by hand**, in `OAuthTokenService.authorize` and
  `AuthService.resolveAuthorizationCode`, because this application does not make them. The
  redirect's decoded payload is the pushed request object, which is where the requested
  `authorization_details` are actually visible.
- **The log is per session, not per page.** That is what lets it survive the trip to the
  authorization server, keeps one customer's tokens out of another's console, and means a flow
  run with the panel closed is still there in full afterwards.
- The recorder is bound to the request thread rather than passed down, because the API calls are
  made by services that never see the session. It is a no-op when nothing is bound, so it is safe
  to call from anywhere.
- **It shows tokens and assertions unredacted** - that is the point of it. Nothing here belongs
  in an application that is not a demonstrator.

## Platform constraints that shape this code

These are WSO2 IS / Carbon behaviours, each of which cost real debugging time. Changing the code
to violate one of them will break the app in a way that does not point back here.

1. **Redeploying this app repeatedly breaks the Identity Server. Restart IS every few
   redeploys.** This is the single most disruptive thing about developing against a webapp
   deployed inside IS, and it is not a bug in this code.

   Each redeploy leaves an unclean teardown behind. Carbon stops the context after the app's
   archive is already gone, so the CXF servlet cannot shut down:

   ```
   ERROR ... Servlet [InitialDataJAXServlet] threw unload() exception
   Caused by: java.lang.IllegalStateException: java.io.IOException: InvocationTargetException
       at org.apache.catalina.webresources.AbstractSingleArchiveResourceSet.getArchiveEntry
   ```

   After enough of those, Carbon's bundle state goes stale and IS's *own* long-lived webapps end
   up holding classloaders that cannot resolve classes they need. It has surfaced twice, in
   different places, and each time only a restart cleared it:
   - `authenticationendpoint`'s JSPs stop compiling, so the IS console can no longer log in.
   - `/oauth2` throws `NoClassDefFoundError: org/wso2/carbon/identity/openidconnect/RequestObjectBuilder`,
     so **`POST /oauth2/par` answers 500** and this app cannot start an authorization at all.
     Verified on 2026-09-09: seven unload/reload cycles, then the next PAR call failed.

   The second one is worth recognising on sight, because a 500 from the PAR endpoint looks exactly
   like a malformed request. Check IS's `wso2carbon.log` for `RequestObjectBuilder` before
   touching the request code.

2. **`META-INF/webapp-classloading.xml` must stay, requesting `Tomcat` only** — but understand
   what it does and does not do. Carbon's default is `Carbon,CXF3`, the shared classloaders IS's
   own webapps use; this app bundles its whole CXF stack and calls no Carbon API, so it has no
   business on them. Keep it for that reason. It does **not** prevent the damage in point 1:
   the isolation was added to fix exactly that and the failure recurred with it in place, in both
   the WAR and the deployed app. Do not treat it as a fix, and do not remove it expecting a
   change either way.
3. **No request path may end in a slash.** Carbon's `RequestNormalizationValve` 302-redirects any
   GET whose URI ends in `/`, and the API gateway then rejects the 302 as a response its schema
   does not define. Build resource URLs as `/accounts` and `/accounts/{id}`, never `/accounts/`.
   (`String.stripTrailing()` strips whitespace, not slashes — it will not save you.)
4. **CXF instantiates `ApiController` per request** (`jaxrs.serviceClasses` →
   `PerRequestResourceProvider`). An instance field cannot carry state between two HTTP
   requests, so anything that must survive the redirect to the authorization server — the PKCE
   code verifier, the pending payment, which flow is in progress — lives in the `HttpSession`
   via `AuthFlowState`. Never move it back into a field or a static.
5. **Serving at the bare context root needs both** `SpaForwardServlet` mapped to `/*` (Carbon
   redirects `/ctx/` → `/ctx` and Tomcat redirects `/ctx` → `/ctx/`, which loops unless a
   servlet claims the root) **and absolute asset URLs** (`vite.config.ts` `base`,
   `api.ts` `baseUrl`, and `resolveAssetUrl` for the paths inside `configurations/config.json`),
   because at the slash-less root a relative `./assets/…` resolves against the server root.
   A relative URL that is read by the browser *after* the router has moved to a deeper route
   resolves correctly, so this failure looks intermittent - an image that appears on a reload of
   `/accounts-central` but not when entering at the context root. Anything new that names a file
   must go through `resolveAssetUrl` and nothing else. `config.json`'s paths are anchored once,
   where the file is fetched, so a page that prefixes a base path of its own on top of that
   yields `/api-ob-demo-1.0.0/api-ob-demo-1.0.0/…` and a broken image: there is one resolver by
   design, and `add-accounts-page.tsx` had a second one that had to go. Static directories are mapped
   back to the container `default` servlet in `web.xml`; keep those mappings in step with the
   folders under `src/main/webapp`.
6. **IS's `AuthenticationValve` is fail-closed**: a URI with no matching
   `[[resource.access_control]]` entry returns 401, not 404. The server-side rule must cover the
   bare context root, e.g. `context = "(.*)/api-ob-demo-1.0.0(/.*)?"`.

## Debugging

`HttpConnection` throws on any non-2xx with the method, URL, status and the server's response
body, and `ApiController` passes that message through, so a failed OAuth or API call names itself
in the browser. Beyond that, the fastest sources of truth are IS's
`repository/logs/http_access_.<date>.log` (shows the whole flow, including this app's own
`/init/*` calls, since it is deployed inside IS) and APIM's `wso2carbon.log` (gateway-side reason
for a rejected API call).

## Known gaps

- **Consent revocation is a no-op.** The non-regulated Accounts API exposes no consent resource
  to delete, so `revokeAccountConsent` logs and returns true; the UI drops the accounts locally
  and the consent expires. Real revocation would need the accelerator's consent admin API.
- **JARM signatures are not verified.** `JwtDecoder` decodes without checking the signature and
  there is no JOSE library in the POM. The access token arrives over this app's own mTLS
  connection, but the JARM response comes through the browser, so a production client must
  verify it against the authorization server's JWKS.
