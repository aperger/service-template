package hu.ps.common.config;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTClaimsSetAwareJWSKeySelector;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

/**
 * https://github.com/sdoxsee/examples/blob/master/multi-tenant-jwt-resourceserver/complex/src/main/java/ca/simplestep/example/resourceserver/jwt/complex/config/tenant/TenantJwtIssuerValidator.java
 *
 * https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/multitenancy.html
 */
@Configuration
@EnableConfigurationProperties(ResourceServerProperties.class)
public class ResourceServerConfig {

    JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = new JwtIssuerAuthenticationManagerResolver
            ("https://idp.example.org/issuerOne", "https://idp.example.org/issuerTwo");

    /*http
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated()
    )
            .oauth2ResourceServer(oauth2 -> oauth2
            .authenticationManagerResolver(authenticationManagerResolver)
            );*/


    private ResourceServerProperties properties;

    public ResourceServerConfig(ResourceServerProperties properties) {
        this.properties = properties;
    }

    /*@Bean
    TenantRepository inMemoryTenantRepository() {
        TenantRepository tenantRepository = new TenantRepository();
        properties.getIssuers().forEach(i -> tenantRepository.save(new Tenant(i)));
        return tenantRepository;
    }*/

    @Bean
    JWTProcessor jwtProcessor(JWTClaimsSetAwareJWSKeySelector keySelector) {
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor =
                new DefaultJWTProcessor();
        jwtProcessor.setJWTClaimsSetAwareJWSKeySelector(keySelector);
        return jwtProcessor;
    }

    @Bean
    JwtDecoder jwtDecoder(JWTProcessor jwtProcessor, OAuth2TokenValidator<Jwt> jwtValidator) {
        NimbusJwtDecoder decoder = new NimbusJwtDecoder(jwtProcessor);
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(),
                jwtValidator
        );
        decoder.setJwtValidator(validator);
        return decoder;
    }
}
