package hu.ps.templates.serviceapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/*
https://www.baeldung.com/spring-security-oauth-resource-server
https://github.com/Baeldung/spring-security-oauth


ResourceServerProxy:
https://laurspilca.com/consuming-an-endpoint-protected-by-an-oauth-2-resource-server-from-a-spring-boot-service/

*/
@Configuration
@EnableWebSecurity
public class OAuth2ResourceServerSecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String issuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;

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
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        // @formatter:on
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        // return JwtDecoders.fromIssuerLocation ("https://office.pergersoft.hu/auth/realms/pssecurity");
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    }

}

