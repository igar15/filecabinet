package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.DocumentTestData;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.ExternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import javax.validation.ConstraintViolationException;

import static com.igar15.filecabinet.ExternalDispatchTestData.*;

class ExternalDispatchServiceImplTest extends AbstractServiceTest {

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @Autowired
    private DocumentService documentService;

    @Test
    void create() {
        ExternalDispatch created = externalDispatchService.create(getNew());
        int newId = created.id();
        ExternalDispatch newExternalDispatch = getNew();
        newExternalDispatch.setId(newId);
        Assertions.assertEquals(newExternalDispatch, created);
        Assertions.assertEquals(externalDispatchService.findById(newId), newExternalDispatch);
    }

    @Test
    void createDuplicateWaybillAndDateAndCompany() {
        Assertions.assertThrows(DataAccessException.class, () -> externalDispatchService.create(getNewWithDuplicateWaybillAndDateAndOutgoingNumber()));
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(externalDispatch -> {
            validateRootCause(PSQLException.class, () -> externalDispatchService.create(externalDispatch));
        });
    }

    @Test
    void findById() {
        ExternalDispatch found = externalDispatchService.findById(EXTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(EXTERNAL_DISPATCH1, found);
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getDocuments().size());
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> externalDispatchService.findById(NOT_FOUND));
    }

    @Test
    void findByIdWithDocuments() {
        ExternalDispatch found = externalDispatchService.findByIdWithDocuments(EXTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(EXTERNAL_DISPATCH1, found);
        Assertions.assertEquals(EXTERNAL_DISPATCH1.getDocuments(), found.getDocuments());
    }

    @Test
    void findAll() {
        Page<ExternalDispatch> page = externalDispatchService.findAll(PAGEABLE);
        Assertions.assertEquals(PAGE, page);
    }

    @Test
    void update() {
        ExternalDispatch updated = getUpdated();
        externalDispatchService.update(updated);
        Assertions.assertEquals(updated, externalDispatchService.findById(EXTERNAL_DISPATCH1_ID));
        Assertions.assertEquals(updated.getDocuments(), externalDispatchService.findByIdWithDocuments(EXTERNAL_DISPATCH1_ID).getDocuments());
    }

    @Test
    void updateWithoutChildren() {
        ExternalDispatch updated = getUpdated();
        updated.setDocuments(null);
        externalDispatchService.updateWithoutChildren(updated);
        ExternalDispatch found = externalDispatchService.findByIdWithDocuments(updated.getId());
        Assertions.assertEquals(getUpdated(), found);
        Assertions.assertNotNull(found.getDocuments());
    }

    @Test
    void addDocumentWithNullDecimalNumber() {
        String errorMessage = externalDispatchService.addDocument(getForAddDocument(), null);
        Assertions.assertEquals(NOT_EMPTY_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addDocumentWithAlreadyAddedDecimalNumber() {
        String errorMessage = externalDispatchService.addDocument(getForAddDocument(), DocumentTestData.DOCUMENT1.getDecimalNumber());
        Assertions.assertEquals(ALREADY_ADDED_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addDocumentWithNotExistDecimalNumber() {
        String errorMessage = externalDispatchService.addDocument(getForAddDocument(), DocumentTestData.NOT_FOUND_DECIMAL_NUMBER);
        Assertions.assertEquals(NOT_FOUND_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addDocument() {
        ExternalDispatch externalDispatch = getForAddDocument();
        externalDispatchService.addDocument(externalDispatch, DocumentTestData.DOCUMENT6.getDecimalNumber());
        externalDispatch.getDocuments().put(DocumentTestData.DOCUMENT6, true);
        ExternalDispatch found = externalDispatchService.findByIdWithDocuments(EXTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(externalDispatch, found);
        Assertions.assertEquals(externalDispatch.getDocuments(), found.getDocuments());
        Assertions.assertTrue(found.getDocuments().get(DocumentTestData.DOCUMENT6));
    }

    @Test
    void removeDocumentSizeEqualsOne() {
        ExternalDispatch externalDispatch = getForRemoveDocumentWithSizeEqualsOne();
        String errorDeleteDocumentMessage = externalDispatchService.removeDocument(externalDispatch, DocumentTestData.DOCUMENT1_ID);
        Assertions.assertEquals(ERROR_DELETE_DOCUMENT_MESSAGE, errorDeleteDocumentMessage);
    }

    @Test
    void removeDocument() {
        ExternalDispatch externalDispatch = getForRemoveDocument();
        externalDispatchService.removeDocument(externalDispatch, DocumentTestData.DOCUMENT1_ID);
        externalDispatch.getDocuments().remove(DocumentTestData.DOCUMENT1);
        ExternalDispatch found = externalDispatchService.findByIdWithDocuments(EXTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(externalDispatch, found);
        Assertions.assertEquals(externalDispatch.getDocuments(), found.getDocuments());
        Assertions.assertFalse(found.getDocuments().containsKey(DocumentTestData.DOCUMENT1));
    }

    @Test
    void deleteById() {
        externalDispatchService.deleteById(EXTERNAL_DISPATCH1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> externalDispatchService.findById(EXTERNAL_DISPATCH1_ID));
        Assertions.assertEquals(DocumentTestData.DOCUMENT1, documentService.findById(DocumentTestData.DOCUMENT1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> externalDispatchService.deleteById(NOT_FOUND));
    }
}