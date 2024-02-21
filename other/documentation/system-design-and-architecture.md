# System Design And Architecture

---

![backend-service-diagram](../../other/images/backend-service-diagram.png)

## Backend Services

The backend layer consists of 8 separate services 1 library module. Each of these services can have multiple instances
deployed for better fault tolerance and availability.

* ### 'api-gateway' service:

  This is the gateway layer of the application. It implements **API Gateway** and **Response Aggregation** pattern.
  All the incoming requests have to go through this layer. This layer will forward the requests to the corresponding
  downstream services. And aggregate the response if necessary before sending to the client.

* ### 'discovery-server' service:

  This is basically a **Eureka Discovery Server**. All the backend services will register themselves to this service.
  Then this service will periodically check the status of those services. So that, the system can identify which
  services are running on which ports. Later, this information will be processed by the **'api-gateway'** service for
  implementing **server side load-balancing / reverse proxy** and **circuit breaker** pattern.

* ### 'config-server' service:

  This is the **Cloud Config Server** of this application. It provides a centralized location for storing the
  configuration settings for the backend services.

* ### 'keycloak-server' service:

  We'll be using **Keycloak** as the auth server for application.

* ### 'user-service':
  This service is responsible for handling all the user-related data and actions. For example, fetching and managing
  user and user profile related data.

* ### 'course-service':
  This service is responsible for dandling all the course related data and actions. For example, creating, fetching
  and managing courses. And approving payment requests, etc.

* ### 'content-delivery-service':
  This service is responsible for storing and retrieving media contents (image, file, video etc.) of the application.

* ### 'messaging-service':
  This service is responsible for sending notifications to the users.

* ### 'common-module':
  This is not a service. Rather, a library module containing all the common helper / util classes for the application.