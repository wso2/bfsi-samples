# WSO2 Open Banking FDX Compliance Reference Implementation

A reference implementation for the [FDX (Financial Data Exchange)](https://financialdataexchange.org/) specification, built on top of the WSO2 Financial Services Accelerator 4.0.0.

## Overview

This toolkit provides extension point implementations and a demo backend to help developers build FDX-compliant Open Banking solutions using WSO2 Identity Server (IS) 7.2.0 and WSO2 API Manager (APIM) 4.6.0. It covers the full consent lifecycle — creation, authorization, retrieval, validation, and revocation — along with token management, DCR, RAR-based authorization detail processing, and event notification flows.

## Repository Structure

```
financial-services-fdx-sample-toolkit/
├── fs-compliance-toolkit-fdx/
│   ├── reference-implementation-openbanking-fdx/   # WSO2 accelerator extension implementations
│   │   └── src/
│   │       ├── gen/java/org/wso2/openbanking/fdx/extensions/
│   │       │   ├── api/                            # Auto-generated JAX-RS API interfaces (OpenAPI Generator)
│   │       │   └── model/                          # Auto-generated request/response model classes
│   │       └── main/
│   │           ├── java/org/wso2/openbanking/fdx/extensions/
│   │           │   ├── impl/consent/               # Consent flow extension implementations
│   │           │   ├── impl/dcr/                   # DCR pre/post processing implementations
│   │           │   ├── utils/                      # FDX utilities and constants
│   │           │   └── exceptions/                 # Custom FDX exception types
│   │           ├── openapi/                        # OpenAPI spec for accelerator extension points
│   │           └── webapp/WEB-INF/                 # Servlet and app configuration
│   ├── org.wso2.financial.services.fdx.identity/   # OSGi bundle for IS-level authorization detail processing
│   │   └── src/main/java/org/wso2/financial/services/fdx/identity/
│   │       ├── authorize/impl/                     # FDX authorization detail processor implementation
│   │       ├── authorize/model/                    # FDX authorization details model
│   │       └── authorize/utils/                    # Authorization detail utilities
│   └── demo-backend/                               # Sample banking services for demos and testing
│       └── src/main/java/com/wso2/openbanking/fdx/demo/backend/
│           ├── services/                           # Account, bank, payment, and funds confirmation endpoints
│           ├── configurations/                     # Application configuration classes
│           └── util/                               # Common utilities and error constants
└── sample-configs/                                 # Sample deployment configuration files
    ├── wso2is-7.2.0-deployment.toml
    └── wso2am-4.6.0-deployment.toml
```

> **Note:** Code under `src/gen/` is auto-generated from the OpenAPI spec in `src/main/openapi/` using the [OpenAPI Generator CLI](https://openapi-generator.tech/). Do not edit files in `src/gen/` directly — regenerate them by running `mvn generate-sources`.

### Modules

| Module | Artifact | Purpose |
|--------|----------|---------|
| `reference-implementation-openbanking-fdx` | `api#reference-implementation#ob#fdx.war` | Implements WSO2 accelerator extension APIs for the FDX consent flow |
| `org.wso2.financial.services.fdx.identity` | `org.wso2.financial.services.fdx.identity-1.0.0.jar` | OSGi bundle deployed into WSO2 IS to process FDX RAR authorization details |
| `demo-backend` | `api#openbanking#fdx#backend.war` | Provides mock banking endpoints (accounts, payments, funds confirmation) for demos and testing |

## Prerequisites

- **Java** 11
- **Maven** 3.x
- **WSO2 Identity Server** 7.2.0 with the Financial Services Accelerator
- **WSO2 API Manager** 4.6.0 (optional, for full API gateway deployment)

## Build

Build all modules from the project root:

```bash
mvn clean install
```

The built artifacts are placed in each module's `target/` directory.

## Deployment

### Reference Implementation

Deploy `api#reference-implementation#ob#fdx.war` to the WSO2 Identity Server's servlet container (TomEE/Tomcat). This registers the FDX extension endpoints that the accelerator invokes during consent flows.

```
https://<IS_HOST>:<PORT>/api/reference-implementation/ob/fdx/
```

### Identity Module

Deploy `org.wso2.financial.services.fdx.identity-1.0.0.jar` as an OSGi bundle into WSO2 Identity Server's `repository/components/dropins/` directory. This registers the `FDXAuthorizationDetailProcessorImpl` component that processes FDX RAR (`authorization_details`) objects during OAuth 2.0 authorization flows.

### Sample Configurations

Reference deployment configuration files for both products are provided under `sample-configs/`:

| File | Product |
|------|---------|
| `wso2is-7.2.0-deployment.toml` | WSO2 Identity Server 7.2.0 |
| `wso2am-4.6.0-deployment.toml` | WSO2 API Manager 4.6.0 |

## Extension Points

The reference implementation covers the following WSO2 accelerator extension hooks:

- **DCR** — pre/post client registration and update processing
- **Consent lifecycle** — creation, retrieval, revocation, authorization screen population, persistence, and access validation
- **Authorization details (RAR)** — `fdx_v1.0` authorization detail type processing and scope-to-data-cluster mapping (via the identity OSGi bundle)
- **Token & authorize flows** — token refresh handling, authorization validation
- **Event notifications** — event creation, subscription management, polling
- **Error handling** — custom error response mapping
- **File operations** — file upload and retrieval

Extension point contracts are defined in `fs-compliance-toolkit-fdx/reference-implementation-openbanking-fdx/src/main/openapi/accelerator-extensions-v1.0.4.yaml`.

## Installation and Setup

1. Configure Accelerator servers (IS and APIM) as per the instructions in the [WSO2 documentation](https://ob.docs.wso2.com/en/latest/get-started/quick-start-guide/).

2. Goto fs-compliance-toolkit-fdx/reference-implementation-openbanking-fdx/target/ folder and host the `api#reference-implementation#ob#fdx.war`
3in a preferred location and get the base URL.

NOTE:
- If you are hosting this in WSO2 Identity Server copy the `api#reference-implementation#ob#fdx.war` to the
- `<IS_HOME>/repository/deployment/server/webapps` folder.
- Add the following configurations to the deployment.toml file inside the `<IS_HOME>/repository/conf` folder.
```
[[resource.access_control]]
allowed_auth_handlers = ["BasicAuthentication"]
context = "(.*)/api/reference-implementation/ob/fdx/(.*)"
http_method = "all"
secure = "true"
```

## Configuring WSO2 Open Banking Accelerator 4.0.0

1. Update the following configurations in the deployment.toml file inside the `<IS_HOME>/repository/conf` folder.

```
[financial_services.extensions.endpoint]
enabled = true
# allowed extensions: "pre_process_client_creation", "pre_process_consent_creation"
allowed_extensions = ["populate_consent_authorize_screen", "persist_authorized_consent", "validate_consent_access",
    "pre_process_consent_retrieval", "pre_process_client_creation", "pre_process_client_update"]
base_url = "https://<HOSTNAME>:<PORT>/api/reference-implementation/ob/fdx/"
retry_count = 5
connect_timeout = 5
read_timeout = 5

[financial_services.extensions.endpoint.security]
# supported types : Basic-Auth or OAuth2
type = "Basic-Auth"
username = "<USERNAME>"
password = "<PASSWORD>"
```

2. Update the following configurations related to Dynamic Client Registration, in the deployment.toml file inside 
the `<IS_HOME>/repository/conf` folder.

```
[[financial_services.app_registration.dcr.params]]
name = "SoftwareId"
key = "software_id"
required = false
include_in_response = true

[[financial_services.app_registration.dcr.params]]
name = "Scope"
key = "scope"
required = false
include_in_response = true
allowed_values = ["fdx:accountbasic:read", "fdx:accountdetail:read", "fdx:transactions:read", "fdx:investments:read", "fdx:transfers:write"]

[[financial_services.app_registration.dcr.params]]
name = "RedirectUris"
key = "redirect_uris"
required = true
include_in_response = true

[[financial_services.app_registration.dcr.params]]
name = "GrantTypes"
key = "grant_types"
required = true
include_in_response = false
allowed_values = ["authorization_code", "refresh_token", "client_credentials"]

[[financial_services.app_registration.dcr.params]]
name = "SoftwareStatement"
key = "software_statement"
required = false
include_in_response = true

[[financial_services.app_registration.dcr.params]]
name = "ApplicationType"
key = "application_type"
required = false
include_in_response = true
allowed_values = ["web"]

[[financial_services.app_registration.dcr.params]]
name = "TokenEndpointAuthMethod"
key = "token_endpoint_auth_method"
required = false
include_in_response = true

[[financial_services.app_registration.dcr.params]]
name = "IdTokenSignatureAlgorithm"
key = "id_token_signed_response_alg"
required = false
include_in_response = true

[[financial_services.app_registration.dcr.params]]
name = "RequestObjectSignatureAlgorithm"
key = "request_object_signing_alg"
required = false
include_in_response = true

[[financial_services.app_registration.dcr.params]]
name = "Iss"
key = "iss"
required = false
include_in_response = false

[[financial_services.app_registration.dcr.params]]
name = "Iat"
key = "iat"
required = false
include_in_response = false

[[financial_services.app_registration.dcr.params]]
name = "Exp"
key = "exp"
required = false
include_in_response = false

[[financial_services.app_registration.dcr.params]]
name = "Jti"
key = "jti"
required = false
include_in_response = false

[[financial_services.app_registration.dcr.params]]
name = "Aud"
key = "aud"
required = false
include_in_response = false

[[financial_services.app_registration.dcr.params]]
name = "ClientName"
key = "client_name"
required = true
include_in_response = true

[[financial_services.app_registration.dcr.validators.validator]]
name = "RequiredParamsValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.RequiredParamsValidator"
enable = true
priority = 1
[[financial_services.app_registration.dcr.validators.validator]]
name = "IssuerValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.IssuerValidator"
enable = false
priority = 2
[[financial_services.app_registration.dcr.validators.validator]]
name = "RedirectUriFormatValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.RedirectUriFormatValidator"
enable = false
priority = 3
[[financial_services.app_registration.dcr.validators.validator]]
name = "RedirectUriMatchValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.RedirectUriMatchValidator"
enable = false
priority = 4
[[financial_services.app_registration.dcr.validators.validator]]
name = "UriHostnameValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.UriHostnameValidator"
enable = false
priority = 5
[[financial_services.app_registration.dcr.validators.validator]]
name = "SSAIssuerValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.SSAIssuerValidator"
enable = false
priority = 6
allowed_values = ["OpenBanking Ltd"]
[[financial_services.app_registration.dcr.validators.validator]]
name = "RequestJTIValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.RequestJTIValidator"
enable = false
priority = 7
[[financial_services.app_registration.dcr.validators.validator]]
name = "SSAJTIValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.SSAJTIValidator"
enable = false
priority = 8
[[financial_services.app_registration.dcr.validators.validator]]
name = "TokenEndpointAuthSigningAlgValidator"
class = "org.wso2.financial.services.accelerator.identity.extensions.client.registration.dcr.validators.TokenEndpointAuthSigningAlgValidator"
enable = false
priority = 9
```

3. Update the scopes for the `pre-initiated` and `scope-based` flows in the deployment.toml file inside the `<IS_HOME>/repository/conf` folder.

```
[financial_services.consent.pre_initiated]
scopes=[]

[financial_services.consent.scope_based]
scopes=["fdx:accountbasic:read", "fdx:transactions:read", "fdx:investments:read", "fdx:transfers:write"]
```

4. Update the `consent_id_claim_name` and `append_consent_id_to_access_token` in the deployment.toml file inside the `<IS_HOME>/repository/conf` folder.

```
[financial_services.identity]
consent_id_claim_name="fdxConsentId"
append_consent_id_to_token_id_token=false
append_consent_id_to_authz_id_token=true
append_consent_id_to_access_token=true
append_consent_id_to_token_introspect_response=false
```

5. Start the IS server.


## Try Out

See [TRYOUT.md](TRYOUT.md) for a step-by-step guide covering DCR, authorization detail type registration, PAR, and the full consent authorization flow.

## License

Copyright (c) WSO2 LLC. All rights reserved.
