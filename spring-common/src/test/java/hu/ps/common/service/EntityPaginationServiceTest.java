package hu.ps.common.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import hu.ps.common.config.InitH2Repositories;
import hu.ps.common.config.JpaRepositoryConfig;
import hu.ps.common.dto.FooDto;
import hu.ps.common.mapper.FooMapper;
import hu.ps.common.model.FooEntity;
import hu.ps.common.repository.FooRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(classes = { JpaRepositoryConfig.class })
// (classes = { H2Config.class, MapperConfig.class, VirusScanService.class, RestTemplate.class })
class EntityPaginationServiceTest extends InitH2Repositories {

    private static class FooPaginationService extends EntityPaginationService<FooEntity, FooDto> {

        private FooRepository repository;
        private FooMapper mapper;

        public FooPaginationService(FooRepository repository, FooMapper mapper) {
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

    EntityPaginationService service;

    @Autowired
    FooRepository repository;

    @Autowired
    FooMapper mapper;


    List<FooEntity> entities = new ArrayList<>();

    private final int ENTITY_COUNT = 100;

    @BeforeEach
    void setUp() {
        init();
        clearRepositories();

        service = new FooPaginationService(repository, mapper);

        var parent = genarateParentEntity("parent");

        for (int i = 0; i < ENTITY_COUNT; i++) {
            var entity =  genarateFooEntity("entity" + (i + 1), parent);
            entities.add(entity);
        }
    }

    @AfterEach
    void tearDown() {
        clearRepositories();
    }


    @Test
    void extractSortDirectionTest() {
        assertEquals(Sort.Direction.ASC, service.extractSortDirection(null));
        assertEquals(Sort.Direction.ASC, service.extractSortDirection("ASC"));
        assertEquals(Sort.Direction.DESC, service.extractSortDirection("desc"));
    }

    @Test
    void getSortByNameOfColsTest() {
        assertEquals("testName", service.getSortByNameOfCols("testName"));
        assertEquals("startDate", service.getSortByNameOfCols("startDateFormatted"));
        assertEquals("endDate", service.getSortByNameOfCols("endDateFormatted"));
    }

    @Test
    void parseSortParamTest() {
        assertEquals(Sort.by("testName").descending(), service.parseSortParam("testName", "desc"));
        assertEquals(Sort.by("startDate").ascending(), service.parseSortParam("startDateFormatted", "ASC"));
        assertEquals(Sort.by("endDate").ascending(), service.parseSortParam("endDateFormatted", null));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.parseSortParam(null, null));
        assertEquals("Property must not be null or empty", exception.getMessage());
    }

    @Test
    void createPageDetailsTest() {

        Sort sort = Sort.by("name").descending();
        Pageable pageable = PageRequest.of(5, 5, sort);
        assertEquals(pageable, service.createPageDetails(5, 5, "name", "desc"));

        sort = Sort.by("startDate").ascending();
        pageable = PageRequest.of(1, 10, sort);
        assertEquals(pageable, service.createPageDetails(1, 10, "startDateFormatted", "asc"));

        sort = Sort.by("endDate").ascending();
        pageable = PageRequest.of(2, 20, sort);
        assertEquals(pageable, service.createPageDetails(2, 20, "endDateFormatted", null));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.createPageDetails(2, 20, null, null));
        assertEquals("Property must not be null or empty", exception.getMessage());
    }

    @Test
    void getPageResultTest() {
        Sort sort = Sort.by("name").descending();
        Pageable pageable = PageRequest.of(4, 20, sort);


        int fromIndex = (int)pageable.getOffset();
        int toIndex = fromIndex + pageable.getPageSize() - 1;

        if (toIndex >= entities.size()) {
            toIndex = entities.size() -1;
        }

        List<FooEntity> sublist = entities.subList(fromIndex, toIndex);
        Page<FooEntity> page = new PageImpl<>(sublist, pageable, entities.size());

        assertEquals(20, service.getPageResult(page).getPageSize());
        assertEquals(4, service.getPageResult(page).getPageNumber());
        assertEquals(entities.size(), service.getPageResult(page).getTotalElements());
    }

    @Test
    void getPageResultDtoTest() {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(3, 10, sort);

        List<FooDto> dtos = mapper.toDtoList(entities);

        int fromIndex = (int)pageable.getOffset();
        int toIndex = fromIndex + pageable.getPageSize() - 1;

        List<FooDto> sublist = dtos.subList(fromIndex, toIndex);
        Page<FooDto> page = new PageImpl<>(sublist, pageable, dtos.size());

        assertEquals(entities.size(), service.getPageResultDto(page).getTotalElements());
        assertEquals(dtos.subList(fromIndex, toIndex), service.getPageResultDto(page).getRows());
    }

    @Test
    void getPageResultDtoWithPaginationTest() {
        Sort sort = Sort.by("name").descending();
        Pageable pageable = PageRequest.of(1, 10, sort);

        List<FooDto> dtos = mapper.toDtoList(entities);

        assertEquals(entities.size(), service.getPageResultDtoWithPagination(dtos, pageable).getTotalElements());
        assertEquals(10, service.getPageResultDtoWithPagination(dtos, pageable).getRows().size());

        pageable = PageRequest.of(10, 10, sort);
        assertEquals(0, service.getPageResultDtoWithPagination(dtos, pageable).getTotalElements());
        assertNull(service.getPageResultDtoWithPagination(dtos, pageable).getRows());
        assertEquals(0, service.getPageResultDtoWithPagination(dtos, pageable).getPageSize());
        assertEquals(0, service.getPageResultDtoWithPagination(dtos, pageable).getPageNumber());
    }

}
