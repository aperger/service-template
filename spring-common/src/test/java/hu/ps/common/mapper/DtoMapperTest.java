package hu.ps.common.mapper;

import hu.ps.common.config.InitH2Repositories;
import hu.ps.common.dto.FooDto;
import hu.ps.common.dto.ParentDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoMapperTest extends InitH2Repositories {

    private static ParentMapper mapper = new ParentMapperImpl(new FooMapperImpl());

    private static List<ParentDto> dtos = new ArrayList<>();
    public static ParentDto createParentDto(Long id, String name) {
        return ParentDto.builder().Id(id).name(name).fooList(new ArrayList<>()).build();
    }

    public static FooDto createFooDto(Long id, String name, ParentDto parent) {
        var object = FooDto.builder().Id(id).name(name).parentId(parent.getId()).build();
        parent.getFooList().add(object);
        return object;
    }

    static private ParentDto parentDto1;
    static private ParentDto parentDto2;
    static private FooDto fooDto11;
    static private FooDto fooDto12;
    static private FooDto fooDto21;
    static private FooDto fooDto22;

    static {
        parentDto1 = createParentDto(1L, "parent1");
        parentDto2 = createParentDto(2L, "parent2");
        fooDto11 = createFooDto(1l, "foo11", parentDto1);
        fooDto12 = createFooDto(2l, "foo12", parentDto1);
        fooDto21 = createFooDto(3l, "foo21", parentDto2);
        fooDto22 = createFooDto(4l, "foo22", parentDto2);

        dtos.add(parentDto1);
        dtos.add(parentDto2);
    }

    @Test
    void toEntity() {
        var entity = mapper.toEntity(parentDto1);

        assertEquals(parentDto1.getId(), entity.getId());
        assertEquals(parentDto1.getName(), entity.getName());

        assertEquals(parentDto1.getFooList().size(), entity.getFooList().size());

        entity.getFooList().forEach(f -> {
            assertNotNull(f.getParent());
            assertEquals(parentDto1.getId(), f.getParent().getId());
            assertNull(f.getParent().getName());
            assertNull(f.getParent().getName());
        });
    }

    @Test
    void toEntityList() {

        var entities = mapper.toEntityList(dtos);

        assertEquals(dtos.size(), entities.size());
    }
}