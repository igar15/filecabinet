package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.service.ExternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;

import static com.igar15.filecabinet.ExternalDispatchTestData.*;

class ExternalDispatchServiceImplTest extends AbstractServiceTest {

    @Autowired
    private ExternalDispatchService externalDispatchService;

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
            validateRootCause(ConstraintViolationException.class, () -> externalDispatchService.create(externalDispatch));
        });
    }

    @Test
    void findById() {
        ExternalDispatch found = externalDispatchService.findById(EXTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(EXTERNAL_DISPATCH1, found);
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> externalDispatchService.findById(NOT_FOUND));
    }

    @Test
    void findAll() {
        Assertions.assertEquals(EXTERNAL_DISPATCHES, externalDispatchService.findAll(pageable));
    }

//    @Test
//    void findAllByDocumentId() {
//        List<ExternalDispatch> allByDocumentId = externalDispatchService.findAllByDocumentId(DocumentTestData.DOCUMENT1_ID);
//        Assertions.assertEquals(List.of(EXTERNAL_DISPATCH1, EXTERNAL_DISPATCH2), allByDocumentId);
//    }

    @Test
    void update() {
        ExternalDispatch updated = getUpdated();
        externalDispatchService.update(updated);
        Assertions.assertEquals(updated, externalDispatchService.findById(EXTERNAL_DISPATCH1_ID));
    }

    @Test
    void deleteById() {
        externalDispatchService.deleteById(EXTERNAL_DISPATCH1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> externalDispatchService.findById(EXTERNAL_DISPATCH1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> externalDispatchService.deleteById(NOT_FOUND));
    }
}