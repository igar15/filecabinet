package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.DepartmentTestData;
import com.igar15.filecabinet.DocumentTestData;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;


import java.util.HashMap;

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
    void findAllWithNameAndDepartmentAndChangeCodeAndAfterAndBeforeExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(NAME_EXAMPLE, DepartmentTestData.DEPARTMENT_3.getName(), CHANGE_CODE_EXAMPLE, AFTER_EXAMPLE, BEFORE_EXAMPLE, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_NAME_AND_DEPARTMENT_AND_CHANGE_CODE_AND_AFTER_AND_BEFORE_EXAMPLE, page);
    }

    @Test
    void findAllWithNameAndAfterAndBeforeExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(NAME_EXAMPLE, null, null, AFTER_EXAMPLE, BEFORE_EXAMPLE, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_NAME_AND_AFTER_AND_BEFORE_EXAMPLE, page);
    }

    @Test
    void findAllWithChangeCodeAndAfterAndBeforeExample() {
        Page<ChangeNotice> page = changeNoticeService.findAll(null, null, CHANGE_CODE_EXAMPLE, AFTER_EXAMPLE, BEFORE_EXAMPLE, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_CHANGE_CODE_AND_AFTER_AND_BEFORE_EXAMPLE, page);
    }

    @Test
    void update() {
        ChangeNotice updated = getUpdated();
        changeNoticeService.update(updated);
        ChangeNotice found = changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID);
        Assertions.assertEquals(updated, found);
        Assertions.assertEquals(updated.getDocuments(), found.getDocuments());
    }

    @Test
    void updateWithoutChildren() {
        ChangeNotice updated = getUpdated();
        changeNoticeService.updateWithoutChildren(updated);
        ChangeNotice found = changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID);
        Assertions.assertEquals(updated, found);
        Assertions.assertEquals(updated.getDocuments(), found.getDocuments());
    }

    @Test
    void addDocumentWithNullDecimalNumber() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, null, "1");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_NULL_DECIMAL_NUMBER[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_NULL_DECIMAL_NUMBER[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentWithNullChangeNumber() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, DocumentTestData.DOCUMENT1.getDecimalNumber(), null);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_NULL_CHANGE_NUMBER[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_NULL_CHANGE_NUMBER[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentWithNullDecimalAndChangeNumbers() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, null, null);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_NULL_DECIMAL_AND_CHANGE_NUMBERS[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_NULL_DECIMAL_AND_CHANGE_NUMBERS[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentWithWrongDecimalNumber() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, DocumentTestData.NOT_FOUND_DECIMAL_NUMBER, "1");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_WRONG_DECIMAL_NUMBER[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_WRONG_DECIMAL_NUMBER[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentWithWrongChangeNumber() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, DocumentTestData.DOCUMENT3.getDecimalNumber(), "0");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_WRONG_CHANGE_NUMBER[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_WRONG_CHANGE_NUMBER[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentWithDuplicateChangeNumber() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, DocumentTestData.DOCUMENT3.getDecimalNumber(), "1");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_DUPLICATE_CHANGE_NUMBER[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_DUPLICATE_CHANGE_NUMBER[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentWithDuplicateDecimalNumber() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, DocumentTestData.DOCUMENT1.getDecimalNumber(), "3");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_DUPLICATE_DECIMAL_NUMBER[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_DUPLICATE_DECIMAL_NUMBER[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentWithInvalidChangeNumber() {
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE1, DocumentTestData.DOCUMENT3.getDecimalNumber(), "first");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_INVALID_CHANGE_NUMBER[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_INVALID_CHANGE_NUMBER[1], results[1]);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void addDocumentForNew() {
        Object[] results = changeNoticeService.addDocument(getNew(), DocumentTestData.DOCUMENT1.getDecimalNumber(), "3");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_OK_DECIMAL_AND_CHANGE_NUMBERS[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_OK_DECIMAL_AND_CHANGE_NUMBERS[1], results[1]);
        ChangeNotice found = changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE_NEW_ID);
        Assertions.assertEquals(getNewWithAddedDocument(), found);
        Assertions.assertEquals(getNewWithAddedDocument().getDocuments(), found.getDocuments());
    }

    @Test
    void addDocumentForNotNew() {
        CHANGE_NOTICE3.setDocuments(new HashMap<>(CHANGE_NOTICE3.getDocuments()));
        Object[] results = changeNoticeService.addDocument(CHANGE_NOTICE3, DocumentTestData.DOCUMENT1.getDecimalNumber(), "3");
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_OK_DECIMAL_AND_CHANGE_NUMBERS[0], results[0]);
        Assertions.assertEquals(ADD_DOCUMENT_RESULTS_FOR_OK_DECIMAL_AND_CHANGE_NUMBERS[1], results[1]);
        ChangeNotice found = changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE3.getId());
        Assertions.assertEquals(getNotNewWithAddedDocument(), found);
        Assertions.assertEquals(getNotNewWithAddedDocument().getDocuments(), found.getDocuments());
    }

    @Test
    void removeDocumentForSizeEqualsOne() {
        String message = changeNoticeService.removeDocument(CHANGE_NOTICE1, DocumentTestData.DOCUMENT1_ID);
        Assertions.assertEquals(REMOVE_DOCUMENT_ERROR_FOR_SIZE_EQUALS_1, message);
        Assertions.assertEquals(1, changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE1_ID).getDocuments().size());
    }

    @Test
    void removeDocument() {
        CHANGE_NOTICE4.setDocuments(new HashMap<>(CHANGE_NOTICE4.getDocuments()));
        String message = changeNoticeService.removeDocument(CHANGE_NOTICE4, DocumentTestData.DOCUMENT2.getId());
        Assertions.assertNull(message);
        ChangeNotice found = changeNoticeService.findByIdWithDocuments(CHANGE_NOTICE4.getId());
        Assertions.assertEquals(getWithRemovedDocument(), found);
        Assertions.assertEquals(getWithRemovedDocument().getDocuments(), found.getDocuments());
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