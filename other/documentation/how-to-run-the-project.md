# How to Run The Project

---

### Step 01: Create 'e-learning-platform' Realm in Keycloak

1. Navigate to `docker-compose` folder. And run the below command to start keycloak server.
    ```bash
        docker compose up
   ```
2. Then visit `http://localhost:8080` to access keycloak admin console. User username `admin` and password `admin` for
   login.

3. After login, create a new realm with [realm-export.json](../config-files/realm-export.json) file.
4. Now you can create some dummy users with `ADMIN` and `USER` role for testing.

### Step 02: Start the Backend Services

Start the below services. Switch to `dev` / `stg` profile per preference. For `stg` profile, local database need to be
created.

1. discovery-server
2. api-gateway
3. user-service
4. course-management-service
5. content-delivery-service