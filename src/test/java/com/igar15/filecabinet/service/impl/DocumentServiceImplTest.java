package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.CompanyTestData;
import com.igar15.filecabinet.DepartmentTestData;
import com.igar15.filecabinet.DocumentTestData;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.igar15.filecabinet.DocumentTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DocumentService documentService;

    @Test
    void create() {
        Document created = documentService.create(getNew());
        int newId = created.id();
        Document newDocument = getNew();
        newDocument.setId(newId);
        Assertions.assertEquals(newDocument, created);
        Assertions.assertEquals(documentService.findById(newId), newDocument);
    }

    @Test
    void createDuplicateDecimalNumber() {
        Assertions.assertThrows(DataAccessException.class, () -> documentService.create(getNewWithDuplicateDecimalNumber()));
    }

    @Test
    void createDuplicateInventoryNumber() {
        Assertions.assertThrows(DataAccessException.class, () -> documentService.create(getNewWithDuplicateInventoryNumber()));
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(document ->  {
            validateRootCause(PSQLException.class, () -> documentService.create(document));
        });
    }

    @Test
    void findById() {
        Document found = documentService.findById(DOCUMENT1_ID);
        Assertions.assertEquals(DOCUMENT1, found);
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getChangeNotices().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getExternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getInternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getApplicabilities().size());
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> documentService.findById(NOT_FOUND_ID));
    }

    @Test
    void findByIdWithChangeNotices() {
        Document found = documentService.findByIdWithChangeNotices(DOCUMENT1_ID);
        Assertions.assertEquals(DOCUMENT1, found);
        Assertions.assertEquals(getWithChangeNotices().getChangeNotices(), found.getChangeNotices());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getApplicabilities().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getExternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getInternalDispatches().size());
    }

    @Test
    void findByIdWithExternalDispatches() {
        Document found = documentService.findByIdWithExternalDispatches(DOCUMENT1_ID);
        Assertions.assertEquals(DOCUMENT1, found);
        Assertions.assertEquals(getWithExternalDispatches().getExternalDispatches(), found.getExternalDispatches());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getApplicabilities().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getChangeNotices().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getInternalDispatches().size());
    }

    @Test
    void findByIdWithInternalDispatches() {
        Document found = documentService.findByIdWithInternalDispatches(DOCUMENT1_ID);
        Assertions.assertEquals(DOCUMENT1, found);
        Assertions.assertEquals(getWithInternalDispatches().getInternalDispatches(), found.getInternalDispatches());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getApplicabilities().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getExternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getChangeNotices().size());

    }

    @Test
    void findByIdWithApplicabilities() {
        Document found = documentService.findByIdWithApplicabilities(DOCUMENT5.getId());
        Assertions.assertEquals(DOCUMENT5, found);
        Assertions.assertEquals(getWithApplicabilities().getApplicabilities(), found.getApplicabilities());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getChangeNotices().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getExternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getInternalDispatches().size());

    }

    @Test
    void findByDecimalNumber() {
        Document found = documentService.findByDecimalNumber(DOCUMENT1_DECIMAL_NUMBER);
        Assertions.assertEquals(DOCUMENT1, found);
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getChangeNotices().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getExternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getInternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getApplicabilities().size());
    }

    @Test
    void findByDecimalNumberNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> documentService.findByDecimalNumber(NOT_FOUND_DECIMAL_NUMBER));
    }

    @Test
    void findByDecimalNumberWithChangeNotices() {
        Document found = documentService.findByDecimalNumberWithChangeNotices(DOCUMENT1_DECIMAL_NUMBER);
        Assertions.assertEquals(DOCUMENT1, found);
        Assertions.assertEquals(getWithChangeNotices().getChangeNotices(), found.getChangeNotices());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getApplicabilities().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getExternalDispatches().size());
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getInternalDispatches().size());
    }

    @Test
    void findAllWithFullDecimalNumber() {
        Page<Document> page = documentService.findAll(FULL_DECIMAL_NUMBER, null, null, null, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_FULL_DECIMAL_NUMBER, page);
    }

    @Test
    void findAllWithPartOfDecimalNumber() {
        Page<Document> page = documentService.findAll(PART_OF_DECIMAL_NUMBER, null, null, null, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_PART_OF_DECIMAL_NUMBER, page);
    }

    @Test
    void findAllWithFullName() {
        Page<Document> page = documentService.findAll(null, DOCUMENT2.getName(), null, null, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_FULL_NAME, page);
    }

    @Test
    void findAllWithPartOfName() {
        Page<Document> page = documentService.findAll(null, PART_OF_NAME, null, null, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(PAGE_FOR_PART_OF_NAME, page);
    }

    @Test
    void findAllWithAllParamsNull() {
        Page<Document> page = documentService.findAll(null, null, null, null, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL);
    }

    @Test
    void findAllWithAllParamsNullExceptAfterAndBefore() {
        Page<Document> page = documentService.findAll(null, null, null, null, null, null,
                null, null, "2011-01-01", "2019-01-01", PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_AFTER_AND_BEFORE);
    }

    @Test
    void findAllForDefineDate() {
        Page<Document> page = documentService.findAll(null, null, null, null, null, null,
                null, null, "2003-01-30", "2003-01-30", PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_DEFINE_DATE);
    }

    @Test
    void findAllWithAllParamsNullExceptOriginalHolder() {
        Page<Document> page = documentService.findAll(null, null, null, CompanyTestData.COMPANY1_NAME, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_ORIGINAL_HOLDER);
    }

    @Test
    void findAllWithAllParamsNullExceptDepartment() {
        Page<Document> page = documentService.findAll(null, null, DepartmentTestData.DEPARTMENT1_NAME, null, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT);
    }

    @Test
    void findAllWithAllParamsNullExceptDepartmentAndOriginalHolder() {
        Page<Document> page = documentService.findAll(null, null, DepartmentTestData.DEPARTMENT1_NAME, CompanyTestData.COMPANY1_NAME, null, null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT_AND_ORIGINAL_HOLDER);
    }

    @Test
    void findAllWithAllParamsNullExceptDepartmentAndOriginalHolderAndAfterAndBefore() {
        Page<Document> page = documentService.findAll(null, null, DepartmentTestData.DEPARTMENT1_NAME, CompanyTestData.COMPANY1_NAME, null, null,
                null, null, "2001-01-30", "2004-01-30", PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT_AND_ORIGINAL_HOLDER_AND_AFTER_AND_BEFORE);
    }

    @Test
    void findAllWithAllParamsNullExceptDepartmentAndAfterAndBefore() {
        Page<Document> page = documentService.findAll(null, null, DepartmentTestData.DEPARTMENT1_NAME, null, null, null,
                null, null, "2001-01-30", "2004-07-30", PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT_AND_AFTER_AND_BEFORE);
    }

    @Test
    void findAllWithAllParamsNullExceptOriginalHolderAndAfterAndBefore() {
        Page<Document> page = documentService.findAll(null, null, null, CompanyTestData.COMPANY3.getName(), null, null,
                null, null, "2001-01-30", "2013-07-30", PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_ORIGINAL_HOLDER_AND_AFTER_AND_BEFORE);
    }

    @Test
    void findAllWithAllParamsNullExceptInventoryNumber() {
        Page<Document> page = documentService.findAll(null, null, null, null, String.valueOf(DOCUMENT1.getInventoryNumber()), null,
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_INVENTORY_NUMBER);
    }

    @Test
    void findAllWithAllParamsNullExceptStatus() {
        Page<Document> page = documentService.findAll(null, null, null, null, null, Status.ACC_COPY.toString(),
                null, null, null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_STATUS);
    }

    @Test
    void findAllWithAllParamsNullExceptStage() {
        Page<Document> page = documentService.findAll(null, null, null, null, null, null,
                Stage.O1.toString(), null, null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_STAGE);
    }

    @Test
    void findAllWithAllParamsNullExceptForm() {
        Page<Document> page = documentService.findAll(null, null, null, null, null, null,
                null, Form.ELECTRONIC.toString(), null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_FORM);
    }

    @Test
    void findAllWithAllParamsNotNullExceptAfterAndBefore() {
        Page<Document> page = documentService.findAll(DOCUMENT1_DECIMAL_NUMBER, DOCUMENT1.getName(), DepartmentTestData.DEPARTMENT_1.getName(), CompanyTestData.COMPANY1.getName(),
                DOCUMENT1.getInventoryNumber().toString(), Status.ORIGINAL.toString(), Stage.O1.toString(), Form.ELECTRONIC.toString(), null, null, PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NOT_NULL_EXCEPT_AFTER_AND_BEFORE);
    }

    @Test
    void findAllWithAllParamsNotNull() {
        Page<Document> page = documentService.findAll(DOCUMENT1_DECIMAL_NUMBER, DOCUMENT1.getName(), DepartmentTestData.DEPARTMENT_1.getName(), CompanyTestData.COMPANY1.getName(),
                DOCUMENT1.getInventoryNumber().toString(), Status.ORIGINAL.toString(), Stage.O1.toString(), Form.ELECTRONIC.toString(), "2001-01-30", "2013-07-30", PAGEABLE);
        Assertions.assertEquals(page, PAGE_FOR_ALL_PARAMS_NOT_NULL);
    }

    @Test
    void update() {
        Document updated = getUpdated();
        documentService.update(updated);
        Document found = documentService.findByIdWithChangeNotices(DOCUMENT1_ID);
        Assertions.assertEquals(updated, found);
        Assertions.assertEquals(updated.getChangeNotices(), found.getChangeNotices());
        found = documentService.findByIdWithExternalDispatches(DOCUMENT1_ID);
        Assertions.assertEquals(updated.getExternalDispatches(), found.getExternalDispatches());
        found = documentService.findByIdWithInternalDispatches(DOCUMENT1_ID);
        Assertions.assertEquals(updated.getInternalDispatches(), found.getInternalDispatches());
        found = documentService.findByIdWithApplicabilities(DOCUMENT1_ID);
        Assertions.assertEquals(updated.getApplicabilities(), found.getApplicabilities());
    }

    @Test
    void updateWithoutChildren() {
        Document updated = getUpdated();
        documentService.update(updated);
        updated.setChangeNotices(null);
        updated.setExternalDispatches(null);
        updated.setInternalDispatches(null);
        updated.setApplicabilities(null);
        documentService.updateWithoutChildren(updated);
        Document found = documentService.findByIdWithChangeNotices(DOCUMENT1_ID);
        Assertions.assertEquals(updated, found);
        Assertions.assertNotNull(found.getChangeNotices());
        found = documentService.findByIdWithExternalDispatches(DOCUMENT1_ID);
        Assertions.assertNotNull(found.getExternalDispatches());
        found = documentService.findByIdWithInternalDispatches(DOCUMENT1_ID);
        Assertions.assertNotNull(found.getInternalDispatches());
        found = documentService.findByIdWithApplicabilities(DOCUMENT1_ID);
        Assertions.assertNotNull(found.getApplicabilities());
    }

    @Test
    void deleteById() {
        documentService.deleteById(DOCUMENT1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> documentService.findById(DOCUMENT1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> documentService.deleteById(NOT_FOUND_ID));
    }



}