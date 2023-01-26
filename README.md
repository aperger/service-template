# REST API service-template
### Resource Server with Spring Boot OAuth 2.0 
##### (micro-service)

Sample resource server secured with OAuth 2.0/OpenID.
- The current configuration uses a Keycloak Authorization server
- The CORS is fully enabled (all sources, all method)
- Open API 3.0 (with Swagger UI) is available, these URL's are publicly available
- a GitHub action is responsible to deploy the service into Azure,
  the event is starting when we push changes into `main` branch 

It is possible to run this service locally. The URL of authorization server need change:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://<keycloak-authorizton-server>/auth/realms/<real-name>/
          jwk-set-uri: https://<keycloak-authorizton-server>/auth/realms/<real-name>/protocol/openid-connect/certs
```

The deployed version is available here:
- https://ps-service-api.azurewebsites.net/
- not all the time is available, sorry :-(

The sample application which uses this resource servert is:
- https://github.com/aperger/app-template