package hu.ps.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "resource-server")
public class ResourceServerProperties {
    Map<String, TenantProperty> tenants = new HashMap<>();
}
