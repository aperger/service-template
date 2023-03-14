package hu.ps.templates.serviceapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

/*
https://www.baeldung.com/spring-security-oauth-resource-server
https://github.com/Baeldung/spring-security-oauth

ResourceServerProxy:
https://laurspilca.com/consuming-an-endpoint-protected-by-an-oauth-2-resource-server-from-a-spring-boot-service/

Need to set for role for the application!
https://learn.microsoft.com/en-us/answers/questions/422202/access-token-validating-fails-with-jwtsecuritytoke
*/
@Configuration
@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration {

    JwtIssuerAuthenticationManagerResolver authenticationManagerResolver = new JwtIssuerAuthenticationManagerResolver
            ("https://login.microsoftonline.com/e31e0e23-29a8-4033-84b7-a7740ca09296/v2.0",
                    "https://office.pergersoft.hu/auth/realms/pssecurity");


    /* need if we use signe tenant, DO NOT DELETE IT!!
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:#{null}(}")
    String issuerUri;
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:#{null}}")
    String jwkSetUri;
    */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http.cors().and()
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/actuator/health**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/message/welcome").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/message/secret/**").hasAuthority("SCOPE_message:read")
                        .requestMatchers(HttpMethod.POST, "/api/message/secret/**").hasAuthority("SCOPE_message:write")
                        .anyRequest().authenticated()
                )
                // need if we use signe tenant
                //.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(authenticationManagerResolver)
                );
        // @formatter:on
        return http.build();
    }


    /* need if we use signe tenant
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }
    */


}

