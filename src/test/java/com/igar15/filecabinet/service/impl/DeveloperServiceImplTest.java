package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Developer;
import com.igar15.filecabinet.service.DeveloperService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.igar15.filecabinet.DeveloperTestData.*;

class DeveloperServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DeveloperService developerService;

    @Test
    void create() {
        Developer created = developerService.create(getNew());
        int newId = created.id();
        Developer newDeveloper = getNew();
        newDeveloper.setId(newId);
        Assertions.assertEquals(newDeveloper, created);
        Assertions.assertEquals(developerService.findById(newId), newDeveloper);
    }

    @Test
    void createDuplicateName() {
        Assertions.assertThrows(DataAccessException.class, () -> developerService.create(getNewWithDuplicateName()));
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(developer -> {
            validateRootCause(ConstraintViolationException.class, () -> developerService.create(developer));
        });
    }

    @Test
    void findById() {
        Developer found = developerService.findById(DEVELOPER1_ID);
        Assertions.assertEquals(DEVELOPER1, found);
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> developerService.findById(NOT_FOUND_ID));
    }

    @Test
    void findByName() {
        Developer found = developerService.findByName(DEVELOPER1_NAME);
        Assertions.assertEquals(DEVELOPER1, found);
    }

    @Test
    void findByNameNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> developerService.findByName(NOT_FOUND_NAME));
    }

    @Test
    void findAll() {
        List<Developer> found = developerService.findAll();
        Assertions.assertEquals(DEVELOPERS, found);
    }

    @Test
    void update() {
        Developer updated = getUpdated();
        developerService.update(updated);
        Assertions.assertEquals(updated, developerService.findById(DEVELOPER1_ID));
    }

    @Test
    void deleteById() {
        developerService.deleteById(DEVELOPER1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> developerService.findById(DEVELOPER1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> developerService.deleteById(NOT_FOUND_ID));
    }

}