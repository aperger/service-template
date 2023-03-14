package hu.ps.templates.reactiveservicetemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerReactiveAuthenticationManagerResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.Collections;

/*
https://www.baeldung.com/spring-security-oauth-resource-server
https://github.com/Baeldung/spring-security-oauth


ResourceServerProxy:
https://laurspilca.com/consuming-an-endpoint-protected-by-an-oauth-2-resource-server-from-a-spring-boot-service/


Need to set for role for the application!
https://learn.microsoft.com/en-us/answers/questions/422202/access-token-validating-fails-with-jwtsecuritytoke
*/

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
public class OAuth2ResourceServerSecurityConfiguration {

    JwtIssuerReactiveAuthenticationManagerResolver authenticationManagerResolver = new JwtIssuerReactiveAuthenticationManagerResolver
            ("https://login.microsoftonline.com/e31e0e23-29a8-4033-84b7-a7740ca09296/v2.0",
                    "https://office.pergersoft.hu/auth/realms/pssecurity");


    /* need if we use signe tenant, DO NOT DELETE IT!!!
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:#{null}(}")
    String issuerUri;
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:#{null}}")
    String jwkSetUri;
    */

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowCredentials(false);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/api-docs/**").permitAll()

                        .pathMatchers("/favicon.ico").permitAll()
                        .pathMatchers("/actuator/health**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/message/secret/**").hasAuthority("SCOPE_message:read")
                        .pathMatchers(HttpMethod.POST, "/api/message/secret/**").hasAuthority("SCOPE_message:write")
                        .anyExchange().authenticated()
                )
                // need if we use signe tenant, DO NOT DELETE IT!!!
                //.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(authenticationManagerResolver)
                );
        // @formatter:on
        return http.build();
    }


    /* need if we use signe tenant, DO NOT DELETE IT!!!
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }
    */


}
