package hu.ps.common.config;

import hu.ps.common.repository.FooRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = FooRepository.class)
@Import({MemoryDBConfig.class, ImportMappers.class})
public class JpaRepositoryConfig {
}
