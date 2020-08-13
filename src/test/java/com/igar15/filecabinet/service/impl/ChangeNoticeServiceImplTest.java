package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.DepartmentTestData;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;


import static com.igar15.filecabinet.ChangeNoticeTestData.*;

class ChangeNoticeServiceImplTest extends AbstractServiceTest {

    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Test
    void create() {
        ChangeNotice created = changeNoticeService.create(getNew());
        int newId = created.id();
        ChangeNotice newChangeNotice = getNew();
        newChangeNotice.setId(newId);
        Assertions.assertEquals(newChangeNotice, created);
        Assertions.assertEquals(changeNoticeService.findById(newId), newChangeNotice);
    }

    @Test
    void createDuplicateName() {
        Assertions.assertThrows(DataAccessException.class, () -> changeNoticeService.create(getNewWithDuplicateName()));
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(changeNotice -> {
            validateRootCause(PSQLException.class, () -> changeNoticeService.create(changeNotice));
        });
    }

    @Test
    void findById() {
        ChangeNotice found = changeNoticeService.findById(CHANGE_NOTICE1_ID);
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getDocuments().size());
        Assertions.assertEquals(CHANGE_NOTICE1, found);
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> changeNoticeService.findById(NOT_FOUND_ID));
    }

    @Test
    void findByIdWithDocuments() {
        ChangeNotice found = changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID);
        Assertions.assertEquals(CHANGE_NOTICE1.getDocuments(), found.getDocuments());
        Assertions.assertEquals(CHANGE_NOTICE1, found);
    }

    @Test
    void findByIdWithDocumentsNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> changeNoticeService.findByIdWithDocuments(NOT_FOUND_ID));
    }

    @Test
    void findByName() {
        ChangeNotice found = changeNoticeService.findByName(CHANGE_NOTICE1_NAME);
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getDocuments().size());
        Assertions.assertEquals(CHANGE_NOTICE1, found);
    }

    @Test
    void findByNameNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> changeNoticeService.findByName(NOT_FOUND_NAME));
    }

    @Test
    void findAllWithNameExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(NAME_EXAMPLE, null, null, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_NAME_EXAMPLE, page);
    }

    @Test
    void findAllWithDepartmentExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(null, DepartmentTestData.DEPARTMENT_1.getName(), null, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_DEPARTMENT_EXAMPLE, page);
    }

    @Test
    void findAllWithChangeCodeExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(null, null, CHANGE_CODE_EXAMPLE, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_CHANGE_CODE_EXAMPLE, page);
    }

    @Test
    void findAllWithAfterExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(null, null, null, AFTER_EXAMPLE, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_AFTER_EXAMPLE, page);
    }

    @Test
    void findAllWithBeforeExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(null, null, null, null, BEFORE_EXAMPLE, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_BEFORE_EXAMPLE, page);
    }

    @Test
    void findAllWithAfterAndBeforeExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(null, null, null, AFTER_EXAMPLE, BEFORE_EXAMPLE, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_AFTER_AND_BEFORE_EXAMPLE, page);
    }

    @Test
    void findAllForDefineDate() {
        Page<ChangeNotice> page = changeNoticeService.findAll(null, null, null, DEFINE_DATE, DEFINE_DATE, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_DEFINE_DATE_EXAMPLE, page);
    }

    @Test
    void findAllWithNameAndDepartmentExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(NAME_EXAMPLE, DepartmentTestData.DEPARTMENT_3.getName(), null, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_NAME_AND_DEPARTMENT_EXAMPLE, page);
    }

    @Test
    void findAllWithNameAndAfterExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(NAME_EXAMPLE, null, null, AFTER_EXAMPLE, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_NAME_AND_AFTER_EXAMPLE, page);
    }

    @Test
    void findAllWithNameAndDepartmentAndChangeCode() {
        Page<ChangeNotice> page = changeNoticeService.findAll(NAME_EXAMPLE, DepartmentTestData.DEPARTMENT_3.getName(), CHANGE_CODE_EXAMPLE, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_NAME_AND_DEPARTMENT_AND_CHANGE_CODE_EXAMPLE, page);
    }

    @Test
    void findAllWithNameAndDepartmentAndChangeCodeAndAfterExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(NAME_EXAMPLE, DepartmentTestData.DEPARTMENT_3.getName(), CHANGE_CODE_EXAMPLE, AFTER_EXAMPLE, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_NAME_AND_DEPARTMENT_AND_CHANGE_CODE_AND_AFTER_EXAMPLE, page);

    }

    @Test
    void update() {
        ChangeNotice updated = getUpdated();
        changeNoticeService.update(updated);
        Assertions.assertEquals(updated, changeNoticeService.findById(CHANGE_NOTICE1_ID));
    }

    @Test
    void deleteById() {
        changeNoticeService.deleteById(CHANGE_NOTICE1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> changeNoticeService.findById(CHANGE_NOTICE1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> changeNoticeService.deleteById(NOT_FOUND_ID));
    }
}