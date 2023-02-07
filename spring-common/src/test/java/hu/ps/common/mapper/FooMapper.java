package hu.ps.common.mapper;

import hu.ps.common.dto.FooDto;
import hu.ps.common.model.FooEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class FooMapper implements EntityMapper<FooEntity, FooDto>, DtoMapper<FooDto, FooEntity> {

    @Override
    @Mapping(source = "parent.id", target = "parentId")
    abstract public FooDto toDto(FooEntity fooEntity);

    @Override
    @Mapping(source = "parentId", target = "parent.id")
    abstract public FooEntity toEntity(FooDto entity);
}
