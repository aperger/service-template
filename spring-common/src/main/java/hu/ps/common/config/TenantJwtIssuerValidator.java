package hu.ps.common.config;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantJwtIssuerValidator implements OAuth2TokenValidator<Jwt> {

    private ResourceServerProperties properties;
    private final Map<String, JwtIssuerValidator> validators = new ConcurrentHashMap<>();

    public TenantJwtIssuerValidator(ResourceServerProperties properties) {
        this.properties = properties;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        return this.validators.computeIfAbsent(toTenant(token), this::fromTenant)
                .validate(token);
    }

    private String toTenant(Jwt jwt) {
        return jwt.getIssuer().toString();
    }

    private JwtIssuerValidator fromTenant(String tenant) {



        return Optional.ofNullable(this.properties.tenants.get(tenant))
                .map(prop -> prop.getJwksUrl())
                .map(JwtIssuerValidator::new)
                .orElseThrow(() -> new IllegalArgumentException("unknown tenant"));
    }
}
