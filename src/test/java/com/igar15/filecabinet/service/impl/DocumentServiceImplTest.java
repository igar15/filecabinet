package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.*;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.repository.ExternalDispatchRepository;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.ExternalDispatchService;
import com.igar15.filecabinet.service.InternalDispatchService;
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

    @Autowired
    private ChangeNoticeService changeNoticeService;

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @Autowired
    private InternalDispatchService internalDispatchService;

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
    void removeChangeWithSizeEqualsOne() {
        Document document = getWithChangeNotices();
        String errorMessage = documentService.removeChange(document, ChangeNoticeTestData.CHANGE_NOTICE1_ID);
        Assertions.assertEquals(REMOVE_CHANGE_ERROR_MESSAGE, errorMessage);
        ChangeNotice changeNotice = changeNoticeService.findByIdWithDocuments(ChangeNoticeTestData.CHANGE_NOTICE1_ID);
        Assertions.assertTrue(changeNotice.getDocuments().containsKey(document));
    }

    @Test
    void removeChange() {
        Document document = getForRemoveChange();
        String errorMessage = documentService.removeChange(document, ChangeNoticeTestData.CHANGE_NOTICE3.getId());
        Assertions.assertNull(errorMessage);
        document.getChangeNotices().remove(1);
        Document found = documentService.findByIdWithChangeNotices(DOCUMENT2.getId());
        Assertions.assertEquals(document, found);
        Assertions.assertEquals(document.getChangeNotices(), found.getChangeNotices());
        ChangeNotice changeNotice = changeNoticeService.findByIdWithDocuments(ChangeNoticeTestData.CHANGE_NOTICE3.getId());
        Assertions.assertFalse(changeNotice.getDocuments().containsKey(document));
    }

    @Test
    void addApplicabilityWithEmptyDecimalNumber() {
        String errorMessage = documentService.addApplicability(DOCUMENT1, null);
        Assertions.assertEquals(ADD_APPLICABILITY_WITH_EMPTY_DECIMAL_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addApplicabilityAlreadyAdded() {
        String errorMessage = documentService.addApplicability(getWithApplicabilities(), DOCUMENT3.getDecimalNumber());
        Assertions.assertEquals(ADD_APPLICABILITY_ALREADY_ADDED_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addApplicabilityNotFound() {
        String errorMessage = documentService.addApplicability(DOCUMENT2, NOT_FOUND_DECIMAL_NUMBER);
        Assertions.assertEquals(ADD_APPLICABILITY_NOT_FOUND_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addApplicability() {
        Document document = getWithApplicabilities();
        documentService.addApplicability(document, "БА4.151.128");
        document.getApplicabilities().add(DOCUMENT1);
        Document found = documentService.findByIdWithApplicabilities(DOCUMENT5.getId());
        Assertions.assertEquals(document, found);
        Assertions.assertEquals(document.getApplicabilities(), found.getApplicabilities());
    }

    @Test
    void removeApplicability() {
        Document document = getWithApplicabilities();
        Document found = documentService.findByIdWithApplicabilities(DOCUMENT5.getId());
        Assertions.assertTrue(found.getApplicabilities().contains(DOCUMENT3));
        documentService.removeApplicability(found.getId(), DOCUMENT3.getId());
        document.getApplicabilities().remove(DOCUMENT3);
        found = documentService.findByIdWithApplicabilities(DOCUMENT5.getId());
        Assertions.assertFalse(found.getApplicabilities().contains(DOCUMENT3));
        found = documentService.findById(DOCUMENT3.getId());
        Assertions.assertEquals(DOCUMENT3, found);
    }

    @Test
    void deregisterExternal() {
        Document document = getWithExternalDispatches();
        Document found = documentService.findByIdWithExternalDispatches(document.getId());
        Assertions.assertTrue(found.getExternalDispatches().get(ExternalDispatchTestData.EXTERNAL_DISPATCH1));
        documentService.deregisterExternal(found.getId(), ExternalDispatchTestData.EXTERNAL_DISPATCH1_ID);
        document.getExternalDispatches().put(ExternalDispatchTestData.EXTERNAL_DISPATCH1, false);
        found = documentService.findByIdWithExternalDispatches(document.getId());
        Assertions.assertEquals(document, found);
        Assertions.assertEquals(document.getExternalDispatches(), found.getExternalDispatches());
        Assertions.assertFalse(found.getExternalDispatches().get(ExternalDispatchTestData.EXTERNAL_DISPATCH1));
        ExternalDispatch externalDispatch = externalDispatchService.findByIdWithDocuments(ExternalDispatchTestData.EXTERNAL_DISPATCH1_ID);
        Assertions.assertFalse(externalDispatch.getDocuments().get(found));
    }

    @Test
    void deregisterExternalWithIncomings() {
        ExternalDispatch externalDispatch = externalDispatchService.findByIdWithDocuments(ExternalDispatchTestData.EXTERNAL_DISPATCH1_ID);
        externalDispatch.getDocuments().values().forEach(bool -> Assertions.assertTrue(bool));
        documentService.deregisterExternalWithIncomings(DOCUMENT1_ID, ExternalDispatchTestData.EXTERNAL_DISPATCH1_ID);
        externalDispatch = externalDispatchService.findByIdWithDocuments(ExternalDispatchTestData.EXTERNAL_DISPATCH1_ID);
        externalDispatch.getDocuments().values().forEach(bool -> Assertions.assertFalse(bool));
        for (int i = 0; i < 5; i++) {
            Document document = documentService.findByIdWithExternalDispatches(DOCUMENT1_ID + i);
            Assertions.assertFalse(document.getExternalDispatches().get(ExternalDispatchTestData.EXTERNAL_DISPATCH1));
        }
    }

    @Test
    void deregisterInternal() {
        Document document = getWithInternalDispatches();
        Document found = documentService.findByIdWithInternalDispatches(document.getId());
        Assertions.assertTrue(found.getInternalDispatches().get(InternalDispatchTestData.INTERNAL_DISPATCH1));
        documentService.deregisterInternal(found.getId(), InternalDispatchTestData.INTERNAL_DISPATCH1_ID);
        document.getInternalDispatches().put(InternalDispatchTestData.INTERNAL_DISPATCH1, false);
        found = documentService.findByIdWithInternalDispatches(document.getId());
        Assertions.assertEquals(document, found);
        Assertions.assertEquals(document.getInternalDispatches(), found.getInternalDispatches());
        Assertions.assertFalse(found.getInternalDispatches().get(InternalDispatchTestData.INTERNAL_DISPATCH1));
        InternalDispatch internalDispatch = internalDispatchService.findByIdWithDocuments(InternalDispatchTestData.INTERNAL_DISPATCH1_ID);
        Assertions.assertFalse(internalDispatch.getDocuments().get(found));
    }

    @Test
    void deregisterAlbum() {
        InternalDispatch internalDispatch = internalDispatchService.findByIdWithDocuments(InternalDispatchTestData.INTERNAL_DISPATCH2.getId());
        internalDispatch.getDocuments().values().forEach(bool -> Assertions.assertTrue(bool));
        documentService.deregisterAlbum(DOCUMENT1_ID, InternalDispatchTestData.INTERNAL_DISPATCH2.getId());
        internalDispatch = internalDispatchService.findByIdWithDocuments(InternalDispatchTestData.INTERNAL_DISPATCH2.getId());
        internalDispatch.getDocuments().values().forEach(bool -> Assertions.assertFalse(bool));
        for (int i = 0; i < 5; i++) {
            Document document = documentService.findByIdWithInternalDispatches(DOCUMENT1_ID + i);
            Assertions.assertFalse(document.getInternalDispatches().get(InternalDispatchTestData.INTERNAL_DISPATCH2));
        }
    }



    @Test
    void deleteById() {
        documentService.deleteById(DOCUMENT1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> documentService.findById(DOCUMENT1_ID));
        ChangeNotice changeNotice = changeNoticeService.findByIdWithDocuments(ChangeNoticeTestData.CHANGE_NOTICE1_ID);
        Assertions.assertFalse(changeNotice.getDocuments().containsKey(DOCUMENT1));
        ExternalDispatch externalDispatch = externalDispatchService.findByIdWithDocuments(ExternalDispatchTestData.EXTERNAL_DISPATCH1_ID);
        Assertions.assertFalse(externalDispatch.getDocuments().containsKey(DOCUMENT1));
        InternalDispatch internalDispatch = internalDispatchService.findByIdWithDocuments(InternalDispatchTestData.INTERNAL_DISPATCH1_ID);
        Assertions.assertFalse(internalDispatch.getDocuments().containsKey(DOCUMENT1));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> documentService.deleteById(NOT_FOUND_ID));
    }
}