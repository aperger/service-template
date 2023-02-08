# REST API service-template
### Resource Server with Spring Boot OAuth 2.0 
##### (micro-service)

A sample resource server in service-template directory, which is secured with OAuth 2.0/OpenID:
- The current configuration uses two authorization server (the URL's are hardcoded)
  - Keycloak
  - Microsoft Online/Live
- The CORS is fully enabled (all sources, all method)
- Open API 3.0 (with Swagger UI) is available, these URL's are publicly available
- a GitHub action is responsible to deploy the service into Azure, the event is starting when we push changes into `main` branch.

It is possible to run this service locally. In the current solution we have only one configurable item in the application. We need to set a valid client id of the registered appliucation of Azure, which uses this resource. This value is store in AZURE_CLIENT_ID enviroment variable.

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          audiences: ${AZURE_CLIENT_ID}
```




The deployed version is available here:
- https://ps-service-api.azurewebsites.net/
- not all the time is available, sorry :-(

The sample application which uses this resource servert is:
- https://github.com/aperger/app-template