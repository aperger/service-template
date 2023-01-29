package hu.ps.common.mapper;

import java.util.List;

public interface EntityMapper<E, D> {
    abstract D toDto(E entity);

    default List<D> toDtoList(List<E> entities) {
        return entities.stream().map(e -> toDto(e)).toList();
    }
}
