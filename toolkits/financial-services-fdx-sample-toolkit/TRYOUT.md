# Tryout flow
This section will walk you through a sample tryout flow for a Data Recipient application onboarding and consent 
authorization using the FDX API. The steps below cover:
1. Registering a Data Recipient application via Dynamic Client Registration (DCR)
2. Registering an FDX-specific authorization detail type in the Identity Server
3. Authorizing the application to use the registered authorization detail type
4. Initiating a Pushed Authorization Request (PAR) with a sample RAR object for a payment use case
5. Redirecting the user to the authorization endpoint to complete consent authorization
6. Exchanging the authorization code for an access token and calling a sample FDX API endpoint with the obtained token

## Pre Steps

Before starting the tryout, ensure you have the following prerequisites in place:

1. Follow the instructions in [Configure IS 7.x as Key Manager](https://ob.docs.wso2.com/en/latest/get-started/configure-is-as-key-manager/) to set up your Identity Server instance for the tryout.
2. Deploy the FDX APIs inside toolkits/financial-services-fdx-sample-toolkit/fdx-apis directory by referencing the [Deploy Open Banking APIs](https://ob.docs.wso2.com/en/latest/get-started/deploy-apis/) documentation.
3. Configure API Resources, Users and Roles in the Identity Server as per the [Configure Users and Roles](https://ob.docs.wso2.com/en/latest/get-started/configure-users-and-roles/) documentation.

## Register DCR Application

Dynamic Client Registration (DCR) allows a Data Recipient application to register itself programmatically with the 
Authorization Server without manual intervention. This is the first step in the FDX onboarding flow and must be 
completed before any consent or data-sharing operations can take place.

Before registering the application, you need to publish the [fdxapi.recipient-registration.yaml](fdx-apis/openapi-300/fdxapi.recipient-registration.yaml). 
Refer [Tryout Dynamic Client Registration](https://ob.docs.wso2.com/en/latest/get-started/dynamic-client-registration/) 
documentation for more details on how to publish the API.

To register a Data Recipient application, use the `POST /fdxrecipientapi/6.5.0/register` endpoint and the request body
captures the application metadata including redirect URIs, scopes, consent duration settings, and optional 
intermediary details (e.g., a Data Access Platform sitting between the Data Recipient and the Authorization Server).

Upon successful registration, the server returns a `client_id` and `client_secret` that the application must use in 
all subsequent OAuth 2.0 flows. The `scope` field in the response confirms which FDX data scopes the application is 
permitted to request.

Sample Request

```bash
curl --location 'https://localhost:8243/fdxv6.5.0recipientapi/6.5.0/register' \
--header 'accept: application/json' \
--header 'x-fapi-interaction-id: c770aef3-6784-41f7-8e0e-ff5f97bddb3a' \
--header 'FDX-API-Actor-Type: BATCH' \
--header 'Content-Type: application/json' \
--data-raw '{
   "client_name":"My Example Client",
   "description":"Recipient Application servicing financial use case requiring permissioned data sharing",
   "redirect_uris":[
      "https://partner.example/callback"
   ],
   "logo_uri":"https://client.example.org/logo.png",
   "client_uri":"https://example.net/",
   "jwks_uri":"https://keystore.openbankingtest.org.uk/0015800001HQQrZAAX/0015800001HQQrZAAX.jwks",
   "grant_types":[
      "authorization_code",
      "client_credentials"
   ],
   "contacts":[
      "support@example.net"
   ],
   "scope":"fdx:accountbasic:read fdx:accountdetail:read fdx:investments:read fdx:transfers:write",
   "duration_type":[
      "TIME_BOUND"
   ],
   "duration_period":365,
   "lookback_period":365,
   "registry_references":[
      {
         "registered_entity_name":"Official recipient name",
         "registered_entity_id":"4HCHXIURY78NNH6JH",
         "registry":"GLEIF"
      }
   ],
   "intermediaries":[
      {
         "name":"Data Access Platform Name",
         "description":"Data Access Platform specializing in servicing permissioned data sharing for Data Recipients",
         "uri":"https://partner.example/",
         "logo_uri":"https://partner.example/logo.png",
         "contacts":[
            "support@partner.com"
         ],
         "registry_references":[
            {
               "registered_entity_name":"Data Access Platform listed company Name",
               "registered_entity_id":"JJH7776512TGMEJSG",
               "registry":"FDX"
            }
         ]
      },
      {
         "name":"Digital Service Provider Name",
         "description":"Digital Service Provider to the Recipient",
         "uri":"https://sub-partner-one.example/",
         "logo_uri":"https://sub-partner-one.example/logo.png",
         "contacts":[
            "support@sub-partner-one.com"
         ],
         "registry_references":[
            {
               "registered_entity_name":"Service Provider listed company Name",
               "registered_entity_id":"9LUQNDG778LI9D1",
               "registry":"GLEIF"
            }
         ]
      }
   ]
}'
```

Sample Response
```
{
    "client_id": "cBco3wzq4uROU_jjQynXywGshZQa",
    "client_secret": "HPBiz3tOguGCpfXSaNHbIhC8bMyMWNsbLFWnE3Xcvv8a",
    "client_secret_expires_at": 0,
    "redirect_uris": [
        "https://partner.example/callback"
    ],
    "grant_types": [
        "authorization_code",
        "client_credentials"
    ],
    "ext_application_version": "v3.0.0",
    "ext_application_owner": "is_admin@wso2.com@carbon.super",
    "ext_application_token_lifetime": 3600,
    "ext_user_token_lifetime": 3600,
    "ext_refresh_token_lifetime": 86400,
    "ext_id_token_lifetime": 3600,
    "ext_pkce_mandatory": false,
    "ext_pkce_support_plain": false,
    "ext_public_client": false,
    "ext_token_type": "true",
    "require_pushed_authorization_requests": false,
    "subject_type": "public",
    "ext_allowed_audience": "organization",
    "scope": "fdx:accountbasic:read fdx:accountdetail:read fdx:investments:read fdx:transfers:write"
}
```

## Register an Authorization Detail Type

FDX uses Rich Authorization Requests (RAR) — defined in RFC 9396 — to carry granular consent data alongside a standard 
OAuth 2.0 authorization request. Before a Data Recipient can include an `authorization_details` object in a PAR or 
authorization request, the corresponding authorization detail type must be registered in the Identity Server and 
linked to an API Resource.

This step creates the `fdx_v1.0` authorization detail type, which carries the FDX-specific consent structure 
(`durationType`, `durationPeriod`, `lookbackPeriod`, `resources`, and `dataClusters`). The JSON Schema embedded in 
the request defines and validates the shape of this object at the IS level, ensuring only well-formed consent requests 
can proceed.

The associated scopes (`fdx:accountbasic:read`, `fdx:accountdetail:read`, etc.) are also registered here and will be 
enforced during token issuance. Use the `POST /api/server/v1/api-resources` endpoint to perform this registration. 
Save the returned `id` — you will need it to authorize the application in the next step.

Sample Request
```bash
curl --location 'https://<IS_HOSTNAME>:<IS_PORT>/api/server/v1/api-resources/' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic <BASIC_AUTH_CREDENTIALS>' \
--data '{
    "name": "FDX Authorization Type",
    "identifier": "fdx_v1",
    "description": "FDX v1.0 authorization details type",
    "requiresAuthorization": true,
    "scopes": [
        {
            "description": "fdx:accountbasic:read",
            "displayName": "fdx:accountbasic:read",
            "name": "fdx:accountbasic:read"
        },
        {
            "description": "fdx:accountdetail:read",
            "displayName": "fdx:accountdetail:read",
            "name": "fdx:accountdetail:read"
        },
        {
            "description": "fdx:transactions:read",
            "displayName": "fdx:transactions:read",
            "name": "fdx:transactions:read"
        },
        {
            "description": "fdx:investments:read",
            "displayName": "fdx:investments:read",
            "name": "fdx:investments:read"
        },
        {
            "description": "fdx:transfers:write",
            "displayName": "fdx:transfers:write",
            "name": "fdx:transfers:write"
        }
    ],
    "authorizationDetailsTypes": [
        {
            "name": "FDX v1.0 Type",
            "type": "fdx_v1.0",
            "description": "Authorization type for FDX v1.0",
            "schema": {
                "type": "object",
                "required": [
                    "type",
                    "consentRequest"
                ],
                "properties": {
                    "type": {
                        "type": "string",
                        "enum": [
                            "fdx_v1.0"
                        ]
                    },
                    "consentRequest": {
                        "type": "object",
                        "required": [
                            "durationType",
                            "durationPeriod",
                            "lookbackPeriod",
                            "resources"
                        ],
                        "properties": {
                            "durationType": {
                                "type": "string",
                                "enum": [
                                    "ONE_TIME",
                                    "PERSISTENT",
                                    "TIME_BOUND"
                                ]
                            },
                            "durationPeriod": {
                                "type": "integer"
                            },
                            "lookbackPeriod": {
                                "type": "integer"
                            },
                            "resources": {
                                "type": "array",
                                "items": {
                                    "type": "object",
                                    "required": [
                                        "resourceType",
                                        "dataClusters"
                                    ],
                                    "properties": {
                                        "resourceType": {
                                            "type": "string",
                                            "enum": [
                                                "ACCOUNT", 
                                                "CUSTOMER",
                                                "DOCUMENT",
                                                "PAYMENT"
                                            ]
                                        },
                                        "dataClusters": {
                                            "type": "array",
                                            "items": {
                                                "type": "string",
                                                "enum": [
                                                    "ACCOUNT_BASIC",
                                                    "ACCOUNT_DETAILED",
                                                    "ACCOUNT_PAYMENTS",
                                                    "BILLS",
                                                    "CUSTOMER_CONTACT",
                                                    "CUSTOMER_PERSONAL",
                                                    "IMAGES",
                                                    "INVESTMENTS",
                                                    "NOTIFICATIONS",
                                                    "PAYMENT_SUPPORT",
                                                    "REWARDS",
                                                    "STATEMENTS",
                                                    "TAX",
                                                    "TRANSACTIONS",
                                                    "TRANSFERS"
                                                ]
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ]
}'
```

Sample Response
```
{
    "id": "0e927a81-6e33-4584-a755-cfc78b644514",
    "name": "FDX Authorization Type",
    "description": "FDX v1.0 authorization details type",
    "identifier": "fdx_v1",
    "type": "BUSINESS",
    "requiresAuthorization": true,
    "scopes": [
        {
            "id": "a8c50fd6-cf99-481e-985f-23f8b41fda41",
            "displayName": "fdx:accountbasic:read",
            "name": "fdx:accountbasic:read",
            "description": "fdx:accountbasic:read"
        },
        {
            "id": "ec1f0026-4ad3-4849-8964-01f50d34f596",
            "displayName": "fdx:accountdetail:read",
            "name": "fdx:accountdetail:read",
            "description": "fdx:accountdetail:read"
        },
        {
            "id": "a608731c-2558-47c4-8b75-7ad1075dbbca",
            "displayName": "fdx:investments:read",
            "name": "fdx:investments:read",
            "description": "fdx:investments:read"
        },
        {
            "id": "15910150-5c55-4d5a-8fd9-cb14baefc977",
            "displayName": "fdx:transactions:read",
            "name": "fdx:transactions:read",
            "description": "fdx:transactions:read"
        },
        {
            "id": "0b5bcede-9c10-4742-b482-76b9cc53c6ac",
            "displayName": "fdx:transfers:write",
            "name": "fdx:transfers:write",
            "description": "fdx:transfers:write"
        }
    ],
    "authorizationDetailsTypes": [
        {
            "id": "22760a0a-d52a-404a-a450-252bf5962ad9",
            "type": "fdx_v1.0",
            "name": "FDX v1.0 Type",
            "description": "Authorization type for FDX v1.0",
            "schema": {
                "type": "object",
                "required": [
                    "type",
                    "consentRequest"
                ],
                "properties": {
                    "type": {
                        "type": "string",
                        "enum": [
                            "fdx_v1.0"
                        ]
                    },
                    "consentRequest": {
                        "type": "object",
                        "required": [
                            "durationType",
                            "durationPeriod",
                            "lookbackPeriod",
                            "resources"
                        ],
                        "properties": {
                            "durationType": {
                                "type": "string",
                                "enum": [
                                    "ONE_TIME",
                                    "PERSISTENT",
                                    "TIME_BOUND"
                                ]
                            },
                            "durationPeriod": {
                                "type": "integer"
                            },
                            "lookbackPeriod": {
                                "type": "integer"
                            },
                            "resources": {
                                "type": "array",
                                "items": {
                                    "type": "object",
                                    "required": [
                                        "resourceType",
                                        "dataClusters"
                                    ],
                                    "properties": {
                                        "resourceType": {
                                            "type": "string",
                                            "enum": [
                                                "ACCOUNT",
                                                "CUSTOMER",
                                                "DOCUMENT",
                                                "PAYMENT"
                                            ]
                                        },
                                        "dataClusters": {
                                            "type": "array",
                                            "items": {
                                                "type": "string",
                                                "enum": [
                                                    "ACCOUNT_BASIC",
                                                    "ACCOUNT_DETAILED",
                                                    "ACCOUNT_PAYMENTS",
                                                    "BILLS",
                                                    "CUSTOMER_CONTACT",
                                                    "CUSTOMER_PERSONAL",
                                                    "IMAGES",
                                                    "INVESTMENTS",
                                                    "NOTIFICATIONS",
                                                    "PAYMENT_SUPPORT",
                                                    "REWARDS",
                                                    "STATEMENTS",
                                                    "TAX",
                                                    "TRANSACTIONS",
                                                    "TRANSFERS"
                                                ]
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ],
    "properties": []
}
```

After registering the authorization details type, you need to assign the new resource to the users to allow them to 
request the scopes define in the new api resource. You can do this by creating a new role, assigning the api resource to 
the role and then assigning the role to the users.

## Retrieve application ID using the Client ID

After DCR, the Identity Server internally creates a Service Provider (SP) for the registered application. To authorize 
the API Resource registered in the previous step against this SP, you first need to look up the SP's internal `id` using 
the `client_id` returned by the DCR endpoint.

Use the `GET /api/server/v1/applications` endpoint with a filter on `clientId`. Replace `cBco3wzq4uROU_jjQynXywGshZQa` 
with the `client_id` from your DCR response. The returned `id` field (e.g., `a3709414-...`) is the SP identifier used 
in the authorization step that follows.

Sample Request
```
curl --location 'https://localhost:9446/api/server/v1/applications?filter=clientId+eq+cBco3wzq4uROU_jjQynXywGshZQa' \
--header 'Authorization: Basic aXNfYWRtaW5Ad3NvMi5jb206d3NvMjEyMw=='
```

Sample Response
```
{
    "totalResults": 1,
    "startIndex": 1,
    "count": 1,
    "applications": [
        {
            "id": "a3709414-1922-49fa-af83-37e54e1c8226",
            "name": "My_Example_Client",
            "description": "Service Provider for application My_Example_Client",
            "applicationVersion": "v3.0.0",
            "clientId": "cBco3wzq4uROU_jjQynXywGshZQa",
            "realm": "",
            "access": "WRITE",
            "self": "/api/server/v1/applications/a3709414-1922-49fa-af83-37e54e1c8226"
        }
    ],
    "links": []
}
```

## Authorize the authorization details type to the created application

With both the SP `id` (from the previous step) and the API Resource `id` (from the registration step) in hand, you can 
now link the `fdx_v1.0` authorization detail type to the Data Recipient application. This grants the application 
permission to request that authorization detail type in a PAR or authorization request.

The `policyIdentifier` is set to `RBAC` and the `scopes` list should match the FDX scopes registered with the API 
Resource. The `authorizationDetailsTypes` array must include `fdx_v1.0`. Replace the application ID in the URL path 
with the SP `id` retrieved in the previous step.

A `200 OK` response confirms the authorization detail type is now enabled for the application.

Sample Request
```bash
curl --location 'https://localhost:9446/api/server/v1/applications/a3709414-1922-49fa-af83-37e54e1c8226/authorized-apis' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic aXNfYWRtaW5Ad3NvMi5jb206d3NvMjEyMw==' \
--data '{
    "id": "0e927a81-6e33-4584-a755-cfc78b644514",
    "policyIdentifier": "RBAC",
    "scopes": [
        "fdx:accountbasic:read",
        "fdx:accountdetail:read",
        "fdx:transactions:read",
        "fdx:investments:read",
        "fdx:transfers:write"
    ],
    "authorizationDetailsTypes": [
        "fdx_v1.0"
    ]
}'
```

Sample Response
```
200 OK
```

## Sample RAR Object for Accounts

The Rich Authorization Request (RAR) object is the FDX-specific consent payload sent by a Data Recipient to describe
exactly what data access it is requesting. For account use cases, the `resourceType` is set to `ACCOUNT` and the
`dataClusters` array includes Account related data clusters.

The `durationType` of `PERSISTENT` means the consent does not expire automatically; `lookbackPeriod` of 90 days governs
how far back historical data may be accessed.

```json
[
    {
        "type": "fdx_v1.0",
        "consentRequest": {
            "durationType": "PERSISTENT",
            "durationPeriod": 365,
            "lookbackPeriod": 90,
            "resources": [
                {
                    "resourceType": "ACCOUNT",
                    "dataClusters": [
                        "ACCOUNT_BASIC",
                        "ACCOUNT_DETAILED",
                        "TRANSACTIONS",
                        "CUSTOMER_PERSONAL",
                        "CUSTOMER_CONTACT"
                    ]
                }
            ]
        }
    }
]
```

## Sample RAR Object for Payments

The Rich Authorization Request (RAR) object is the FDX-specific consent payload sent by a Data Recipient to describe 
exactly what data access it is requesting. For payment use cases, the `resourceType` is set to `PAYMENT` and the 
`dataClusters` array includes `TRANSFERS`.

The `paymentInfo` sub-object provides the specific payment intent details: the source account (`fromAccountId`), the 
payee (`toPayeeId`), the amount, the merchant account, and the due date. This information is displayed to the end user 
on the consent screen for explicit authorization.

The `durationType` of `ONETIME` means the consent can be used only once; `lookbackPeriod` of 90 days governs 
how far back historical data may be accessed.

```json
[
   {
      "type":"fdx_v1.0",
      "consentRequest":{
         "durationType":"ONETIME",
         "durationPeriod":365,
         "lookbackPeriod":90,
         "resources":[
            {
               "resourceType":"PAYMENT",
               "dataClusters":[
                  "TRANSFERS"
               ],
               "paymentInfo":{
                  "fromAccountId":"ACCOUNT-123",
                  "toPayeeId":"PAYEE-ABC",
                  "amount":10.99,
                  "merchantAccountId":"MERCHANT-ACCOUNT-ID-0001",
                  "dueDate":"2021-08-17"
               }
            }
         ]
      }
   }
]
```

## Initiate PAR

Pushed Authorization Requests (PAR) — defined in RFC 9126 — improve security by sending the full authorization request 
to the server before redirecting the user, rather than encoding it in the browser URL. The server returns a short-lived 
`request_uri` that is used in the subsequent browser redirect.

For FDX, the PAR request must include:
- A signed `request` JWT (the Request Object) containing the authorization parameters including the `authorization_details` 
- RAR object.
- A `client_assertion` JWT for client authentication (private key JWT method).
- The `authorization_details` as a URL-encoded JSON array matching the consent structure registered earlier.

The `request_uri` returned in the response is valid for 60 seconds (as indicated by `expires_in`). You must initiate 
the browser-based authorization redirect within this window.

**Note:** Replace `{{request_object}}` and `{{client_assertion}}` with the actual signed JWTs generated using the Data 
Recipient's private key. Sample values are provided below the curl command for reference.

```bash
curl --location 'https://localhost:9446/oauth2/par' \
--header 'Accept: */*' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'request={{request_object}}' \
--data-urlencode 'client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer' \
--data-urlencode 'client_assertion={{client_assertion}}' \
--data-urlencode 'client_id=cBco3wzq4uROU_jjQynXywGshZQa' \
--data-urlencode 'authorization_details=[
    {
        "type": "fdx_v1.0",
        "consentRequest": {
            "durationType": "PERSISTENT",
            "durationPeriod": 365,
            "lookbackPeriod": 90,
            "resources": [
                {
                    "resourceType": "ACCOUNT",
                    "dataClusters": [
                        "ACCOUNT_BASIC",
                        "ACCOUNT_DETAILED",
                        "TRANSACTIONS",
                        "CUSTOMER_PERSONAL",
                        "CUSTOMER_CONTACT"
                    ]
                }
           ]
       }
    }
]'
```

Sample Request Object JWT

```
eyJ0eXAiOiJKV1QiLCJraWQiOiJzQ2VrTmdTV0lhdVEzNGtsUmhER3Fmd3BqYzQiLCJhbGciOiJQUzI1NiJ9.eyJpYXQiOjE3ODEyNTIzMjQsIm5iZiI6MTc4MTI1MjMyOCwiZXhwIjoxNzgxMjU1OTI4LCJqdGkiOiJUS29MZFI4QUNXOUQtLTFfTVBhQi0iLCJhdWQiOiJodHRwczovL2xvY2FsaG9zdDo5NDQ2L29hdXRoMi90b2tlbiIsImlzcyI6ImNCY28zd3pxNHVST1VfampReW5YeXdHc2haUWEiLCJzY29wZSI6Im9wZW5pZCBmZHg6YWNjb3VudGJhc2ljOnJlYWQiLCJhdXRob3JpemF0aW9uX2RldGFpbHMiOlt7InR5cGUiOiJmZHhfdjEuMCIsImNvbnNlbnRSZXF1ZXN0Ijp7ImR1cmF0aW9uVHlwZSI6IlBFUlNJU1RFTlQiLCJkdXJhdGlvblBlcmlvZCI6MzY1LCJsb29rYmFja1BlcmlvZCI6OTAsInJlc291cmNlcyI6W3sicmVzb3VyY2VUeXBlIjoiQUNDT1VOVCIsImRhdGFDbHVzdGVycyI6WyJBQ0NPVU5UX0JBU0lDIiwiQUNDT1VOVF9ERVRBSUxFRCIsIlRSQU5TQUNUSU9OUyIsIkNVU1RPTUVSX1BFUlNPTkFMIiwiQ1VTVE9NRVJfQ09OVEFDVCJdfV19fV0sImNsYWltcyI6eyJpZF90b2tlbiI6eyJhY3IiOnsidmFsdWVzIjpbInVybjpjZHMuYXU6Y2RyOjMiXSwiZXNzZW50aWFsIjp0cnVlfX0sInVzZXJpbmZvIjp7fX0sInJlc3BvbnNlX3R5cGUiOiJjb2RlIGlkX3Rva2VuIiwicmVkaXJlY3RfdXJpIjoiaHR0cHM6Ly9wYXJ0bmVyLmV4YW1wbGUvY2FsbGJhY2siLCJzdGF0ZSI6InN1aXRlIiwibm9uY2UiOiI4ZmM0Y2JiNC0yODdiLTQyYWEtYTFkMC02N2RjZTZmYzc0NzkiLCJjbGllbnRfaWQiOiJjQmNvM3d6cTR1Uk9VX2pqUXluWHl3R3NoWlFhIiwiY29kZV9jaGFsbGVuZ2UiOiI4V2Rid3ZYblJZbHk0Q0otR3JZQjJhNl9MNEwtMFpXWE1tam5EaXR0YVV3IiwiY29kZV9jaGFsbGVuZ2VfbWV0aG9kIjoiUzI1NiJ9.X8yc-i36lvyVDXkp0yF9yWAI6sQLaTxkLSukVH-kOIB7nScUVMiaEZ_TRbpjYHfXl_YVAWEJr1BQZJaq61IDvlKAIjC9sqRuVfxIKqk4wKt0JgJH-i0nBt0cwbtIS3sieSVhRIAoU6CQls0saxfiCNh-jlz--BLS4JqusN9QPMSv_EB7MhIZ9TOt8DdrTmFZ2un9V8kx1_kN0x_UAX692mBn4NwgGmr_l3BpgcSggOg6z8Xc1ZWhB1SEDKDkLK9t_69c64FhETT7NMdCNM_AYh9AHxCbR18WsQhSUm6MicGv4ppEEq-K5EquKsN4PzglY2vkUuNhG-OJKu9kYNSw5Q
```

Sample Request Object Format
```
{
    "typ": "JWT",
    "kid": "sCekNgSWIauQ34klRhDGqfwpjc4",
    "alg": "PS256"
}
{
    "iat": 1781252324,
    "nbf": 1781252328,
    "exp": 1781255928,
    "jti": "TKoLdR8ACW9D--1_MPaB-",
    "aud": "https://localhost:9446/oauth2/token",
    "iss": "cBco3wzq4uROU_jjQynXywGshZQa",
    "scope": "openid fdx:accountbasic:read",
    "authorization_details": [
        {
            "type": "fdx_v1.0",
            "consentRequest": {
                "durationType": "PERSISTENT",
                "durationPeriod": 365,
                "lookbackPeriod": 90,
                "resources": [
                    {
                        "resourceType": "ACCOUNT",
                        "dataClusters": [
                            "ACCOUNT_BASIC",
                            "ACCOUNT_DETAILED",
                            "TRANSACTIONS",
                            "CUSTOMER_PERSONAL",
                            "CUSTOMER_CONTACT"
                        ]
                    }
                ]
            }
        }
    ],
    "claims": {
    "id_token": {
        "acr": {
            "values": [
                "urn:cds.au:cdr:3"
            ],
            "essential": true
        }
    },
    "userinfo": {}
    },
    "response_type": "code id_token",
    "redirect_uri": "https://partner.example/callback",
    "state": "suite",
    "nonce": "8fc4cbb4-287b-42aa-a1d0-67dce6fc7479",
    "client_id": "cBco3wzq4uROU_jjQynXywGshZQa",
    "code_challenge": "8WdbwvXnRYly4CJ-GrYB2a6_L4L-0ZWXMmjnDittaUw",
    "code_challenge_method": "S256"
}
```
Sample Client Assertion JWT

```
eyJ0eXAiOiJKV1QiLCJraWQiOiJzQ2VrTmdTV0lhdVEzNGtsUmhER3Fmd3BqYzQiLCJhbGciOiJQUzI1NiJ9.eyJpYXQiOjE3ODEyNTIzMjgsIm5iZiI6MTc4MTI1MjMyNCwiZXhwIjoxNzgxMjU1OTI4LCJqdGkiOiIxNzgxMjUyMzI4NjYxIiwic3ViIjoiY0JjbzN3enE0dVJPVV9qalF5blh5d0dzaFpRYSIsImF1ZCI6Imh0dHBzOi8vbG9jYWxob3N0Ojk0NDYvb2F1dGgyL3Rva2VuIiwiaXNzIjoiY0JjbzN3enE0dVJPVV9qalF5blh5d0dzaFpRYSJ9.Ltl3frUdnSTWfmibHjM3S3eksoeqMvyCplYv-Jqpk-d-CYU4YlyfMUB059XdN9KqUe13QbJPSrAyc7A7vD__pGIK7wESxWUqJLcstVzgCtZxjUhl9djvX9iLRhS4sKdCa-nOuhzNRoh0TF8cydWXRoWwrM4VHGLywIAczJohkemlya-qeDT2rsIoYyCC4SvsVKt3Hn-TOf_AMu7dcT5SIZqwcCWuAdmEZkc41yeNbpTIwIa9e8180F65SzcnWJTNQKZblxRB-wGBDWjvhgX4-KLBf3lpojnrz8edMCI8Ny9vAPAcEo6FufM02QcbJWim8khWPFPLGhuGm4Ycs-rMXg
```
Sample Client Assertion Format
```
{
    "typ": "JWT",
    "kid": "sCekNgSWIauQ34klRhDGqfwpjc4",
    "alg": "PS256"
}
{
    "iat": 1781252328,
    "nbf": 1781252324,
    "exp": 1781255928,
    "jti": "1781252328661",
    "sub": "cBco3wzq4uROU_jjQynXywGshZQa",
    "aud": "https://localhost:9446/oauth2/token",
    "iss": "cBco3wzq4uROU_jjQynXywGshZQa"
}
```

Sample Response
```
{
    "expires_in": 60,
    "request_uri": "urn:ietf:params:oauth:par:request_uri:a754d6a6-bc5d-4e66-8faa-d23129298164"
}
```

## Authorize Request

With the `request_uri` obtained from the PAR step, redirect the end user's browser to the Authorization Server's 
`/oauth2/authorize` endpoint. The Identity Server will load the pre-registered authorization request, display the 
FDX consent screen showing the requested data clusters and payment details, and prompt the user to approve or deny the 
consent.

Replace `request_uri` with the value returned by your PAR call. The `client_id` must match the one from your DCR 
registration. Upon user approval, the server redirects to the `redirect_uri` with an authorization `code` and 
`id_token` (for `code id_token` response type). This code is exchanged for an access token in the standard OAuth 2.0 
token endpoint flow.

```
https://localhost:9446/oauth2/authorize?client_id=cBco3wzq4uROU_jjQynXywGshZQa&request_uri=urn:ietf:params:oauth:par:request_uri:a754d6a6-bc5d-4e66-8faa-d23129298164
```

1. Run the above URL in a browser to prompt the invocation of the authorize API. Use the login credentials of a
user that has a consumer role.
![user-login-page](resources/user-login-page.png)

2. Upon successful authentication, the user is redirected to the consent authorize page. First page displays a list of 
bank accounts. Select accounts and click `Next` to proceed to the next page.
![account-selection-page](resources/account-selection-page.png)

3. Data requested by the consent such as permissions, duration period, suration type etc. are displayed on the next page. 
Click `Confirm` to grant these permissions.
![consent-confirm-page](resources/consent-confirm-page.png)

4. 4.Upon providing consent, an authorization code is generated on the web page of the redirect_uri. See the sample given below:
```
https://partner.example/callback#id_token=eyJ4NXQiOiItM3hTQTRjNXh3S05qZ0FpS01VUHF6Y1M5SVEiLCJraWQiOiJZVFpoWVRVM05URmtaVEkwTkdJd016UXlPV1UxTkdRd05tRTJPVEV5TldZMFpETmhZalJsTlRoa01HSmtPVEZsWXpjeFpqUmpPVGd3WkdRM05HWXdNd19QUzI1NiIsImFsZyI6IlBTMjU2In0.eyJpc2siOiJiMTBlNDZlMjRhYTNjNGJlMjAwNzA0ODk1YjgxYjRiODViMTE5Mzg5MmM2ODE2NDkyNDU3ZGUzMzY3YTVhOTFhIiwic3ViIjoiZmU4NmUyOTktNWZmMC00OWYxLWE0MjctYmJmMjBlMTQwNzI4QGNhcmJvbi5zdXBlciIsImFtciI6WyJCYXNpY0F1dGhlbnRpY2F0b3IiXSwiaXNzIjoiaHR0cHM6XC9cL2xvY2FsaG9zdDo5NDQ2XC9vYXV0aDJcL3Rva2VuIiwibm9uY2UiOiI4ZmM0Y2JiNC0yODdiLTQyYWEtYTFkMC02N2RjZTZmYzc0NzkiLCJzaWQiOiJkNTcwMjAwNy1lMTZkLTQxN2ItYTNlZC00YWQwNzA0NzI4Y2MiLCJhdWQiOiJjQmNvM3d6cTR1Uk9VX2pqUXluWHl3R3NoWlFhIiwiYWNyIjoidXJuOm1hY2U6aW5jb21tb246aWFwOnNpbHZlciIsImNfaGFzaCI6IjQ3bE1hNG1tcnYwVFo1NHQwVVFNUlEiLCJzX2hhc2giOiItT3ZVUGI3eDhDbV9TVERHWEp0OFhBIiwiYXpwIjoiY0JjbzN3enE0dVJPVV9qalF5blh5d0dzaFpRYSIsIm9yZ19pZCI6IjEwMDg0YThkLTExM2YtNDIxMS1hMGQ1LWVmZTM2YjA4MjIxMSIsImV4cCI6MTc4MTI1NTk3Niwib3JnX25hbWUiOiJTdXBlciIsImlhdCI6MTc4MTI1MjM3NiwianRpIjoiOTE3YTQ0YmUtMzljOS00OTEwLWJmOTEtMGU3MTEwMzExZmVlIiwib3JnX2hhbmRsZSI6ImNhcmJvbi5zdXBlciIsImZkeENvbnNlbnRJZCI6IjQzZWE2YTI5LTEwZjAtNGFhZC1hNDUxLWNlNGNhNzMyZTljOSJ9.jGDJF2E46NRxkzRFGokNhks8jrJMjCBnOcWp8HA1cvRQaX2Wx87M3dIlOpJ6CaDastvPdsMl-zqKKMnlfWz6YLyXrj4fTVdsTVUCZ-zYrOadbN3Yp2BOoJDIFr-La-4FQn5cdJ8mReo50UlYPrJ04DC4NhuvTxacnXSHWl1noSacRGwv07_-VBJMWZSb9ZFHaSSyvEfsebXUwe5TM0ZZ2Oj4K4U_ISypv8dzd71QwWh2QZiPd91FFhP3L-smEFeCjTODupNt8bJ2zAfKQ3w1w9ALw5IdcCrSTykQp6ZOSXXg_Q7U2Wxkh4HKQEQQoC5gkJw3Qr6pAyAeFoJfQXb6zg&code=5df248ff-0048-3c78-9958-113e2ade6963&session_state=444b54490be8c32673a2058790824db3f6bc08e61f492999a891fb4353991ee1.mAB3VG9IChAVYDhjcM3NnQ&state=suite
```
The authorization code from the below URL is in the code parameter (code=5df248ff-0048-3c78-9958-113e2ade6963).

## Generate user access token

You can generate a user access token using the sample request given below:
```bash
curl --location 'https://localhost:9446/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'X-External-Traffic: true' \
--header 'Cookie: sessionNonceCookie-b0b92c5c-6481-4959-96b6-29b026a644c9=f2eb22e3-a2d7-47de-995a-47d9f9268087' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'scope=openid fdx:accountbasic:read' \
--data-urlencode 'client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer' \
--data-urlencode 'client_assertion={{client_assertion}}' \
--data-urlencode 'redirect_uri=https://partner.example/callback' \
--data-urlencode 'code=5df248ff-0048-3c78-9958-113e2ade6963' \
--data-urlencode 'code_verifier=wMBm~4M51.3v2YnjGyB72MFOxM0C8wcCZomlyqqcQzVy1d_HOdue~uZMfkQ5rvP7XIr41jFL4foK78kvI5M0WFaeStkbZi_R7kYhf-BXq4bu3dU~91.jcFxnt~iBwGPP' \
--data-urlencode 'client_id=cBco3wzq4uROU_jjQynXywGshZQa'
```

Sample Client Assertion JWT
```
eyJ0eXAiOiJKV1QiLCJraWQiOiJzQ2VrTmdTV0lhdVEzNGtsUmhER3Fmd3BqYzQiLCJhbGciOiJQUzI1NiJ9.eyJpYXQiOjE3ODEyNTI2MDQsIm5iZiI6MTc4MTI1MjYwMCwiZXhwIjoxNzgxMjU2MjA0LCJqdGkiOiIxNzgxMjUyNjA0MDU5Iiwic3ViIjoiY0JjbzN3enE0dVJPVV9qalF5blh5d0dzaFpRYSIsImF1ZCI6Imh0dHBzOi8vbG9jYWxob3N0Ojk0NDYvb2F1dGgyL3Rva2VuIiwiaXNzIjoiY0JjbzN3enE0dVJPVV9qalF5blh5d0dzaFpRYSJ9.SYy4ga_iJic-Li_WWJWRGHeiCkW8MajpqpxH6VqdKvw8mcVZkaE-i_8_lGI-lRkWdQK4ZTB2fOTyil2Ei6elciWBJ4eU5Uc_7FV2HEnp85MdyIRnmTvuueWb44_cWPoQMJ74Lli_2ofElLF4diKccV7hdblEfv3iW8htEx8OV7SiaduIvPOaozVCsaS5TWzK0NE2y27rnQTaU6zjLiB25c6f0TQj63Hn1Eyu9E6s-T-bFLLRcxyVbIkoVFCoFjhXVafaZsN1UPezIgYgoHV2t3biwGXGss-ff2yEY7IX3BiqJ_jILiamXUSdULzJxpZUOYy1LvZ-HMxVEqYMLv60jw
```

Sample Client Assertion Format
```
{
   "typ": "JWT",
   "kid": "sCekNgSWIauQ34klRhDGqfwpjc4",
   "alg": "PS256"
}
{
   "iat": 1781252604,
   "nbf": 1781252600,
   "exp": 1781256204,
   "jti": "1781252604059",
   "sub": "cBco3wzq4uROU_jjQynXywGshZQa",
   "aud": "https://localhost:9446/oauth2/token",
   "iss": "cBco3wzq4uROU_jjQynXywGshZQa"
}
```

The response contains a user access token as below. It also includes the unique ID of the consent related to the 
request as `fdxConsentId`.
```
{
    "access_token": "eyJ4NXQiOiItM3hTQTRjNXh3S05qZ0FpS01VUHF6Y1M5SVEiLCJraWQiOiJZVFpoWVRVM05URmtaVEkwTkdJd016UXlPV1UxTkdRd05tRTJPVEV5TldZMFpETmhZalJsTlRoa01HSmtPVEZsWXpjeFpqUmpPVGd3WkdRM05HWXdNd19SUzI1NiIsInR5cCI6ImF0K2p3dCIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJmZTg2ZTI5OS01ZmYwLTQ5ZjEtYTQyNy1iYmYyMGUxNDA3MjhAY2FyYm9uLnN1cGVyIiwiYXV0IjoiQVBQTElDQVRJT05fVVNFUiIsImJpbmRpbmdfdHlwZSI6ImNlcnRpZmljYXRlIiwiaXNzIjoiaHR0cHM6XC9cL2xvY2FsaG9zdDo5NDQ2XC9vYXV0aDJcL3Rva2VuIiwiY2xpZW50X2lkIjoiY0JjbzN3enE0dVJPVV9qalF5blh5d0dzaFpRYSIsImF1ZCI6ImNCY28zd3pxNHVST1VfampReW5YeXdHc2haUWEiLCJuYmYiOjE3ODEyNTI2MDQsImF6cCI6ImNCY28zd3pxNHVST1VfampReW5YeXdHc2haUWEiLCJvcmdfaWQiOiIxMDA4NGE4ZC0xMTNmLTQyMTEtYTBkNS1lZmUzNmIwODIyMTEiLCJhdXRob3JpemF0aW9uX2RldGFpbHMiOlt7InR5cGUiOiJmZHhfdjEuMCIsImNvbnNlbnRSZXF1ZXN0Ijp7ImR1cmF0aW9uVHlwZSI6IlBFUlNJU1RFTlQiLCJkdXJhdGlvblBlcmlvZCI6MzY1LCJsb29rYmFja1BlcmlvZCI6OTAsInJlc291cmNlcyI6W3sicmVzb3VyY2VUeXBlIjoiQUNDT1VOVCIsImRhdGFDbHVzdGVycyI6WyJBQ0NPVU5UX0JBU0lDIiwiQUNDT1VOVF9ERVRBSUxFRCIsIlRSQU5TQUNUSU9OUyIsIkNVU1RPTUVSX1BFUlNPTkFMIiwiQ1VTVE9NRVJfQ09OVEFDVCJdfV19fV0sInNjb3BlIjoiZmR4OmFjY291bnRiYXNpYzpyZWFkIG9wZW5pZCIsImNuZiI6eyJ4NXQjUzI1NiI6IjJFZ29wSEY2Y2JiWHQ5NUk1OEU2OVZFV0JUWllnRmlyQlU5dWdkNkQ5REEifSwiZXhwIjoxNzgxMjU2MjA0LCJvcmdfbmFtZSI6IlN1cGVyIiwiaWF0IjoxNzgxMjUyNjA0LCJiaW5kaW5nX3JlZiI6IjZiZGExYWUxNDgwOTk0MDQyNjllNjcyYTRjYmU4YmFlIiwianRpIjoiZjI2OWFlMzEtNTViMS00MTk1LWJhMDgtMWVlYmYxNDlkOWY3IiwiZmR4Q29uc2VudElkIjoiNDNlYTZhMjktMTBmMC00YWFkLWE0NTEtY2U0Y2E3MzJlOWM5Iiwib3JnX2hhbmRsZSI6ImNhcmJvbi5zdXBlciJ9.QApGb_0324_vba_tAK0YiRJX2L1oKOn0uj4NEGTBLvaJsD8oA5RYrwHe-fZLmNRzClJTQgWIb-Gjoqpa7NpuMCeIMzMgBqsYD4b7cY8BnwOHJT5TJdW1aHx2elWY5c93wP_cqtaWcw2Yt4KT4nbVSAMjo03kEV0syPildBT0t10u7RY8BUY5_QXdm4DbxyADVpH8rUXR6klUEVYf0lXeFezHbe9_r9qNrM1RE-6zmT0sq44lKv8NQZH8cZ5Q_yPze0WQHblVvyJeT3nFfjOBDfejEF9P-11mXYZSwXWuL-MK6Iv8oXCBLrb4xfHenhw-WMTtMwsNu9c2EpNBPlu67Q",
    "authorization_details": [
        {
            "type": "fdx_v1.0",
            "consentRequest": {
                "resources": [
                    {
                        "dataClusters": [
                            "ACCOUNT_BASIC",
                            "ACCOUNT_DETAILED",
                            "TRANSACTIONS",
                            "CUSTOMER_PERSONAL",
                            "CUSTOMER_CONTACT"
                        ],
                        "resourceType": "ACCOUNT"
                    }
                ],
                "durationPeriod": 365,
                "durationType": "PERSISTENT",
                "lookbackPeriod": 90
            }
        }
    ],
    "scope": "fdx:accountbasic:read openid",
    "id_token": "eyJ4NXQiOiItM3hTQTRjNXh3S05qZ0FpS01VUHF6Y1M5SVEiLCJraWQiOiJZVFpoWVRVM05URmtaVEkwTkdJd016UXlPV1UxTkdRd05tRTJPVEV5TldZMFpETmhZalJsTlRoa01HSmtPVEZsWXpjeFpqUmpPVGd3WkdRM05HWXdNd19QUzI1NiIsImFsZyI6IlBTMjU2In0.eyJpc2siOiJiMTBlNDZlMjRhYTNjNGJlMjAwNzA0ODk1YjgxYjRiODViMTE5Mzg5MmM2ODE2NDkyNDU3ZGUzMzY3YTVhOTFhIiwiYXRfaGFzaCI6IlBoazE5RGQ4bWV4NjFCaWFhUzAta2ciLCJzdWIiOiJmZTg2ZTI5OS01ZmYwLTQ5ZjEtYTQyNy1iYmYyMGUxNDA3MjhAY2FyYm9uLnN1cGVyIiwiYW1yIjpbIkJhc2ljQXV0aGVudGljYXRvciJdLCJpc3MiOiJodHRwczpcL1wvbG9jYWxob3N0Ojk0NDZcL29hdXRoMlwvdG9rZW4iLCJub25jZSI6IjhmYzRjYmI0LTI4N2ItNDJhYS1hMWQwLTY3ZGNlNmZjNzQ3OSIsImF1ZCI6ImNCY28zd3pxNHVST1VfampReW5YeXdHc2haUWEiLCJhY3IiOiJ1cm46bWFjZTppbmNvbW1vbjppYXA6c2lsdmVyIiwiY19oYXNoIjoiNDdsTWE0bW1ydjBUWjU0dDBVUU1SUSIsIm5iZiI6MTc4MTI1MjYwNCwiYXpwIjoiY0JjbzN3enE0dVJPVV9qalF5blh5d0dzaFpRYSIsIm9yZ19pZCI6IjEwMDg0YThkLTExM2YtNDIxMS1hMGQ1LWVmZTM2YjA4MjIxMSIsImV4cCI6MTc4MTI1NjIwNCwib3JnX25hbWUiOiJTdXBlciIsImlhdCI6MTc4MTI1MjYwNCwianRpIjoiMzkyMWI0ZDQtMjdkZC00NTEzLTk2ZGMtNmI4NjJmNWIzZTIxIiwib3JnX2hhbmRsZSI6ImNhcmJvbi5zdXBlciJ9.YeIDaorCpFJmxyCRqXcMW_NdWRWRmTo-T-bBu_ykEgKSN8Ee5Q3Yd6CQ8h_eAQwxciigHSuYE1jrRMF9HEfHsuZzAepKCrwU-cXIiLChrIDc6z7iuGWdem-DFW1qrpzbiVsX_dSbWcO_w00SRe07AK-V4Vhbq8SDKtH3rJG3dfbOOSyJYEqvnbtqr-k7CE9KqdbVfqUssWi84zC_UTqXG1E9klP9kistcGaYomHfpmu_QYaiY6MSedy3f0hBlzzIyANa6e4gDIaLTv-Zu22UQpbucaQduLOGIeXqFlS5tZ6RJZ6TrKDeMPR51q9K_cXaeiLXZF5jTEIVQK5VPi1bOw",
    "token_type": "Bearer",
    "expires_in": 3600,
    "fdxConsentId": "43ea6a29-10f0-4aad-a451-ce4ca732e9c9"
}
```

- You can use the user access token to make API calls to the resource server and access the financial data of the user 
as per the consented permissions.

- You can use the user access token and the `fdxConsentId` to make API calls to the resource server to retrieve and revoke the consent.

## Retrieve Consent

You can retrieve the consent details using the `fdxConsentId` obtained from the token response as below:

```bash
curl --location 'https://localhost:8243/fdxv6.0.0consentapi/6.0.0/consents/<fdxConsentId>' \
--header 'Authorization: Bearer <USER_ACCESS_TOKEN>' \
--header 'x-fapi-interaction-id: <INTERACTION_ID>' 
```

Sample Response

```json
{
    "updatedTime": "2026-06-24T06:53:11Z",
    "lookbackPeriod": 90,
    "createdTime": "2026-06-24T06:53:11Z",
    "resources": [
        {
            "dataClusters": [
                "ACCOUNT_BASIC",
                "ACCOUNT_DETAILED",
                "TRANSACTIONS",
                "CUSTOMER_PERSONAL",
                "CUSTOMER_CONTACT"
            ],
            "resourceId": "9deab8ca-9e2b-430f-8e08-d46ee6debc0b",
            "resourceType": "ACCOUNT"
        }
    ],
    "links": [
        {
            "action": "GET",
            "href": "/consents/c8ba8734-72f9-4c73-802e-689b1331b5fa"
        }
    ],
    "id": "c8ba8734-72f9-4c73-802e-689b1331b5fa",
    "durationType": "PERSISTENT",
    "status": "ACTIVE"
}
```

## Accounts Retrieval

Once the user approves the account consent, the TPP is eligible to access the account details of the user. The TPP can now 
invoke the GET /accounts endpoint using the user access token received in the previous step.

Sample Request
```bash
curl --location 'https://localhost:8243/fdxv6.0.0coreapi/6.0.0/accounts' \
--header 'accept: application/json' \
--header 'x-fapi-interaction-id: c770aef3-6784-41f7-8e0e-ff5f97bddb3a' \
--header 'FDX-API-Actor-Type: BATCH' \
--header 'Authorization: Bearer <USER_ACCESS_TOKEN>' \
```

Sample Response
```json
{
    "page": {
        "nextOffset": "2",
        "totalElements": 3
    },
    "links": {
        "next": {
            "href": "/accounts?offSet=2&limit=10"
        }
    },
    "accounts": [
        {
            "accountCategory": "DEPOSIT_ACCOUNT",
            "accountId": "30080012343456",
            "accountType": "CHECKING",
            "accountNumberDisplay": "XXXX4443",
            "nickname": "My Checking Acc XXXX4443",
            "status": "OPEN",
            "balanceType": "ASSET",
            "currency": {
                "currencyCode": "USD"
            },
            "balanceAsOf": "2017-11-05T13:15:30.751Z",
            "currentBalance": 332.22,
            "openingDayBalance": 100.0,
            "availableBalance": 332.22
        }
    ]
}
```
