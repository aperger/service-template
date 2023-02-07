package hu.ps.common.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public abstract class EntityBaseService<T, D> {

    public abstract <ID, R extends JpaRepository<T,ID>, JpaSpecificationExecutor>  R getRepository();
    public abstract Class<T> getEntityClass();
    public abstract Class<D> getDtoClass();
    public abstract D getDto(T entity);
    public List<D> getDtoList(List<T> list) {
        return list.stream().map(this::getDto).collect(Collectors.toList());
    }

    public <ID> Optional<T> findEntity(ID id) {
        return getRepository().findById(id);
    }

    public <ID> T getEntity(ID id) {
        Optional<T> entityOptional = findEntity(id);
        if (entityOptional.isEmpty()) {
            var className = getEntityClass().getName();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Unable to find " + className + " by id: " + id);
        }
        return entityOptional.get();
    }
}
