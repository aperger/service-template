# REST API service-template
### Resource Server with Spring Boot OAuth 2.0 
##### (micro-services)

There are sample resource servers (servlet-api - Jakarta Servelet implementation, reactive-api -  Spring WebFlux implementation), which are secured with OAuth 2.0/OpenID:
- In the current configuration the sample client WEB-application uses two authorization server (the URL's are hardcoded)
  - Keycloak
  - Microsoft Online/Live
- The CORS is fully enabled (all sources, all method)
- Open API 3.0 (with Swagger UI) is available, these URL's are publicly available
- a GitHub actions are responsible to deploy the services into Azure, the events are starting when we push changes into `main` branch.

It is possible to run these services locally. In the current solution we have only one configurable item in the application. We need to set a valid client id of the registered application of Azure, which uses these resources. This value is stored in AZURE_CLIENT_ID enviroment variable.

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          audiences: ${AZURE_CLIENT_ID}
```


### Deployments
_**Not all the time there are available, ... sorry!**_

The deployed versions are available here:
- https://ps-servlet-api.azurewebsites.net/
- https://ps-reactive-api.azurewebsites.net/

The sample application is here, which uses these resource servers:
- https://github.com/aperger/app-template
