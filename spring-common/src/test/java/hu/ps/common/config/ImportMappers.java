package hu.ps.common.config;

import hu.ps.common.mapper.FooMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {FooMapper.class})
public class ImportMappers {
}
