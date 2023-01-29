package hu.ps.common.mapper;

import java.util.List;

public interface DtoMapper<D, E> {

    abstract E toEntity(D entity);

    default List<E> toEntityList(List<D> entities) {
        return entities.stream().map(e -> toEntity(e)).toList();
    }

}
