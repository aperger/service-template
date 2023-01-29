package hu.ps.common.repository;

import hu.ps.common.model.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParentRepository extends JpaRepository<ParentEntity, Long>, JpaSpecificationExecutor<ParentEntity> {
}
