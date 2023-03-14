package hu.ps.templates.reactiveservicetemplate.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(BuildProperties buildProperties) {
        final var version = buildProperties.getVersion() != null ? buildProperties.getVersion() : "v0.0.0";
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
                .title("REST Service API Template")
                .description("Template project to create resource server (micro-service)")
                .version(version)
                .contact(new io.swagger.v3.oas.models.info.Contact()
                        .name("Attila Perger")
                        .url("https://www.linkedin.com/in/attila-perger-636990a7/")
                        .email("perger.attila.ps@gmail.com"))
        );
    }
}
