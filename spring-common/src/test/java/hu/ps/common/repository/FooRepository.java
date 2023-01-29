package hu.ps.common.repository;

import hu.ps.common.model.FooEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FooRepository extends JpaRepository<FooEntity, Long>, JpaSpecificationExecutor<FooEntity> {
}
