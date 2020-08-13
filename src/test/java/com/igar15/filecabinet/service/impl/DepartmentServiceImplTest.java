package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.igar15.filecabinet.DepartmentTestData.*;

class DepartmentServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    void create() {
        Department created = departmentService.create(getNew());
        int newId = created.id();
        Department newDepartment = getNew();
        newDepartment.setId(newId);
        Assertions.assertEquals(newDepartment, created);
        Assertions.assertEquals(departmentService.findById(newId), newDepartment);
    }

    @Test
    void createDuplicateName() {
        Assertions.assertThrows(DataAccessException.class, () -> departmentService.create(getNewWithDuplicateName()));
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(developer -> {
            validateRootCause(PSQLException.class, () -> departmentService.create(developer));
        });
    }

    @Test
    void findById() {
        Department found = departmentService.findById(DEPARTMENT1_ID);
        Assertions.assertEquals(DEPARTMENT_1, found);
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> departmentService.findById(NOT_FOUND_ID));
    }

    @Test
    void findByName() {
        Department found = departmentService.findByName(DEPARTMENT1_NAME);
        Assertions.assertEquals(DEPARTMENT_1, found);
    }

    @Test
    void findByNameNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> departmentService.findByName(NOT_FOUND_NAME));
    }

    @Test
    void findAll() {
        List<Department> found = departmentService.findAll();
        Assertions.assertEquals(DEPARTMENTS, found);
    }

    @Test
    void findAllWithPageable() {
        Page<Department> page = departmentService.findAll(PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_ALL, page);
    }

    @Test
    void findAllByCanTakeAlbums() {
        List<Department> departments = departmentService.findAllByCanTakeAlbums(true);
        Assertions.assertEquals(DEPARTMENTS_ALL_CAN_TAKE_ALBUMS, departments);
    }

    @Test
    void findAllByIsDeveloper() {
        List<Department> departments = departmentService.findAllByIsDeveloper(true);
        Assertions.assertEquals(DEPARTMENTS_ALL_IS_DEVELOPER, departments);
    }

    @Test
    void update() {
        Department updated = getUpdated();
        departmentService.update(updated);
        Assertions.assertEquals(updated, departmentService.findById(DEPARTMENT1_ID));
    }

    @Test
    void deleteById() {
        departmentService.deleteById(DEPARTMENT1_ID + 5);
        Assertions.assertThrows(NotFoundException.class, () -> departmentService.findById(DEPARTMENT1_ID + 5));
    }

    @Test
    void deleteByIdWithExistReferences() {
        Assertions.assertThrows(DataAccessException.class, () -> departmentService.deleteById(DEPARTMENT1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> departmentService.deleteById(NOT_FOUND_ID));
    }

}