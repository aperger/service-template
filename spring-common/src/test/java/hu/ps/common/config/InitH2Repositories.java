package hu.ps.common.config;

import hu.ps.common.model.FooEntity;
import hu.ps.common.model.ParentEntity;
import hu.ps.common.repository.FooRepository;
import hu.ps.common.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

abstract public class InitH2Repositories {

    @Autowired
    FooRepository fooRepository;

    @Autowired
    ParentRepository parentRepository;


    protected List<JpaRepository<?, ?>> repositories = new ArrayList<>();

    protected void init() {
        repositories.clear();

        repositories.add(fooRepository);
        repositories.add(parentRepository);
    }
    public void clearRepositories() {
        init();
        repositories.forEach( r -> {
            r.deleteAll();
            r.flush();
        });
    }

    public ParentEntity genarateParentEntity(String name) {
        var entity = ParentEntity.builder().name(name).build();
        return parentRepository.save(entity);
    }

    public FooEntity genarateFooEntity(String name, ParentEntity parent) {
        var entity = FooEntity.builder().name(name).parent(parent).build();
        return fooRepository.save(entity);
    }

}
