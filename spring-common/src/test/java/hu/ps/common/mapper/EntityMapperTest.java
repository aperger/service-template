package hu.ps.common.mapper;

import hu.ps.common.dto.FooDto;
import hu.ps.common.dto.ParentDto;
import hu.ps.common.model.FooEntity;
import hu.ps.common.model.ParentEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EntityMapperTest {

    static private FooMapper fooMapper = new FooMapperImpl();
    static private ParentMapper parentMapper = new ParentMapperImpl(fooMapper);

    static private ParentEntity parent;

    public static ParentEntity createParentEntity(Long id, String name) {
        return ParentEntity.builder().Id(id).name(name).fooList(new ArrayList<>()).build();
    }
    public static FooEntity createFooEntity(Long id, String name, ParentEntity parent) {
        var object = FooEntity.builder().Id(id).name(name).parent(parent).build();
        parent.getFooList().add(object);
        return object;
    }

    static {
        parent = createParentEntity(1001l, "parent1001");

        createFooEntity(2001L, "Foo 2001", parent);
        createFooEntity(2002L, "Foo 2002", parent);
        createFooEntity(2002L, "Foo 2003", parent);
    }

    @Test
    void toDto() {
        var dto = parentMapper.toDto(parent);
        assertEquals(parent.getId(), dto.getId());
        assertEquals(parent.getName(), dto.getName());
    }

    @Test
    void toDtoList() {
        var dtos = fooMapper.toDtoList(parent.getFooList());
        assertEquals(parent.getFooList().size(), dtos.size());
        for (int i = 0; i < parent.getFooList().size(); i++) {
            var f = parent.getFooList().get(i);
            assertEquals(f.getId(), dtos.get(i).getId());
            assertEquals(f.getName(), dtos.get(i).getName());
            assertEquals(f.getParent().getId(), dtos.get(i).getParentId());
        }
    }
}