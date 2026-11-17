# endlabexam — Spring Boot JWT Validation

## Requirements
- Java 17+
- Maven 3.8+

## Run
From the `endlabexam` folder:

```bash
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Step 1 — JWT validation function
`JwtService.validateAndExtractClaims(token)` uses JJWT to:
1. Parse the signed JWT.
2. Verify its signature using the configured HMAC secret.
3. Reject expired tokens (and malformed/invalidly signed tokens).
4. Return the embedded claims payload.

## Step 2 — Validation endpoint
`GET /api/jwt/validate` expects this header:

```http
Authorization: Bearer <JWT>
```

A valid token returns `valid`, `subject`, timestamps, and extracted claims.
A missing, invalid, or expired token returns HTTP 401.

`POST /api/jwt/demo-token?username=student` creates a short-lived demo token.
This endpoint exists only to make the lab exercise easy to test. Remove or secure it
before using the application in production.

## Step 3 — Test using Swagger UI
1. Start the application and open Swagger UI.
2. Expand `POST /api/jwt/demo-token`, click **Try it out**, enter `student`, and execute.
3. Copy the `token` value from the response.
4. Click **Authorize** at the top of Swagger UI.
5. Paste the token (Swagger's HTTP Bearer scheme adds the `Bearer` prefix).
6. Expand `GET /api/jwt/validate` and click **Try it out** then **Execute**.
7. Confirm the response contains `valid: true`, subject `student`, and claims such as `role` and `scope`.
8. To test failure, change one character in the token or wait until it expires; the endpoint should return HTTP 401.

## Important security note
The secret in `application.properties` is a lab-only example. Do not commit real signing
secrets to source control. Use environment variables or a secret manager in real deployments.
