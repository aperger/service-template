package hu.ps.common.service;

import hu.ps.common.config.InitH2Repositories;
import hu.ps.common.config.JpaRepositoryConfig;
import hu.ps.common.dto.FooDto;
import hu.ps.common.mapper.FooMapper;
import hu.ps.common.model.FooEntity;
import hu.ps.common.model.ParentEntity;
import hu.ps.common.repository.FooRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest(classes = { JpaRepositoryConfig.class })
class EntityBaseServiceTest extends InitH2Repositories {

    private static class FooEntityService extends EntityBaseService<FooEntity, FooDto> {

        FooRepository repository;

        FooMapper mapper;

        public FooEntityService(FooRepository repository, FooMapper mapper) {
            this.repository = repository;
            this.mapper = mapper;
        }

        @Override
        public FooRepository getRepository() {
            return repository;
        }

        @Override
        public Class<FooEntity> getEntityClass() {
            return FooEntity.class;
        }

        @Override
        public Class<FooDto> getDtoClass() {
            return FooDto.class;
        }

        @Override
        public FooDto getDto(FooEntity entity) {
            return mapper.toDto(entity);
        }
    }

    EntityBaseService service;

    @Autowired
    FooRepository repository;

    @Autowired
    FooMapper mapper;

    ParentEntity parent1;

    FooEntity entity1;
    FooEntity entity2;

    @BeforeEach
    void setUp() {

        init();
        service = new FooEntityService(repository, mapper);
        parent1 = genarateParentEntity("parent1");

        entity1 = genarateFooEntity("foo1", parent1);
        entity2 = genarateFooEntity("foo2", parent1);
    }

    @AfterEach
    void tearDown() {
        clearRepositories();
    }

    @Test
    void getInitTest() {
        assertNotNull(service, "EntityBaseService is not created");
        assertNotNull(parent1, "ParentEntity instance is not created");
        assertNotNull(entity1, "FooEntity instance is not created");
        assertNotNull(entity2, "FooEntity instance is not created");
    }

    @Test
    void getDtoListTest() {
        List<FooEntity> entities = new ArrayList<>();
        entities.add(entity1);

        assertNotNull(service.getDtoList(entities));
        assertEquals(mapper.toDto(entity1), service.getDtoList(entities).get(0));
    }

    @Test
    void findEntityTest() {
        Optional<FooEntity> optionalSzervezet = Optional.ofNullable(entity1);
        assertEquals(optionalSzervezet, service.findEntity(entity1.getId()));

        Exception exception = assertThrows(ResponseStatusException.class, () -> service.getEntity(555555555L));
        assertEquals(HttpStatus.NOT_FOUND +
                " \"Unable to find " + entity1.getClass().getName() + " by id: 555555555\"",
                exception.getMessage());
    }

    @Test
    void getEntityTest() {
        assertEquals(entity1, service.getEntity(entity1.getId()));
        Exception exception = assertThrows(ResponseStatusException.class, () -> service.getEntity(55555L));
        assertEquals(HttpStatus.NOT_FOUND +
                " \"Unable to find " + entity1.getClass().getName() + " by id: 55555\"",
                exception.getMessage());
    }

}
