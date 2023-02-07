package hu.ps.common.mapper;

import hu.ps.common.dto.ParentDto;
import hu.ps.common.model.ParentEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {FooMapper.class}
)
public abstract class ParentMapper
        implements EntityMapper<ParentEntity, ParentDto>, DtoMapper<ParentDto, ParentEntity> {
}
