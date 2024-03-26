# Future Improvement Scopes

---

### 1. Implement Dynamic Error Handling in 'api-gateway' Service

**Current Issue**: All of our services throw proper error response when a particular operation fails. But currently
this error message is being ignored in the 'api-gateway' service. And a fixed failure message is sent. This is not an
ideal solution.

**Proposed Action**: We need to fix this issue. And pass the same error response and status code received from
downstream microservice.

### 2. Add Custom Error Message for 401 / 403 Cases

**Current Issue**: Currently we have handle4xxException() method in
the [GlobalExceptionHandler.java](../../common-module/src/main/java/com/devrezaur/common/module/exception/GlobalExceptionHandler.java)
to catch 401 / 403 related cases. But it is not triggering as expected. Because in case of 401 / 403, the request
doesn't reach to controller level. Instead, it is blocked by keycloak at web security level. Hence, the
handle4xxException() method currently has no impact.

**Proposed Action**: Need to do some R&D and handle 401 / 403 cases properly. 