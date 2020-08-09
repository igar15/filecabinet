package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.DocumentTestData;
import com.igar15.filecabinet.InternalDispatchTestData;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.repository.InternalDispatchRepository;
import com.igar15.filecabinet.service.InternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static com.igar15.filecabinet.InternalDispatchTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class InternalDispatchServiceImplTest extends AbstractServiceTest {

    @Autowired
    private InternalDispatchService internalDispatchService;

    @Autowired
    private InternalDispatchRepository internalDispatchRepository;

    @Test
    void check() {
        List<InternalDispatch> byDecimalNumber = internalDispatchRepository.findByDecimalNumber(1009);
        byDecimalNumber.forEach(internalDispatch -> System.out.println(internalDispatch.getWaybill()));

    }

    @Test
    void create() {
        InternalDispatch created = internalDispatchService.create(getNew());
        int newId = created.id();
        InternalDispatch newInternalDispatch = getNew();
        newInternalDispatch.setId(newId);
        Assertions.assertEquals(newInternalDispatch, created);
        Assertions.assertEquals(internalDispatchService.findById(newId), newInternalDispatch);
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(internalDispatch -> {
            validateRootCause(ConstraintViolationException.class, () -> internalDispatchService.create(internalDispatch));
        });
    }

    @Test
    void createDuplicateStampAndDocument() {
        Assertions.assertThrows(DataAccessException.class, () -> internalDispatchService.create(getNewWithDuplicateStampAndDocument()));
    }

    @Test
    void findById() {
        InternalDispatch found = internalDispatchService.findById(INTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(INTERNAL_DISPATCH1, found);
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> internalDispatchService.findById(NOT_FOUND));
    }

    @Test
    void findAll() {
        Assertions.assertEquals(INTERNAL_DISPATCHES, internalDispatchService.findAll());
    }

//    @Test
//    void findAllByDocumentId() {
//        Assertions.assertEquals(List.of(INTERNAL_DISPATCH2, INTERNAL_DISPATCH1), internalDispatchService.findAllByDocumentId(DocumentTestData.DOCUMENT1_ID));
//    }

//    @Test
//    void findByIsAlbum() {
//        Assertions.assertEquals(List.of(INTERNAL_DISPATCH2, INTERNAL_DISPATCH3), internalDispatchService.findByIsAlbum(true));
//    }

    @Test
    void update() {
        InternalDispatch updated = getUpdated();
        internalDispatchService.update(updated);
        Assertions.assertEquals(updated, internalDispatchService.findById(INTERNAL_DISPATCH1_ID));
    }

    @Test
    void deleteById() {
        internalDispatchService.deleteById(INTERNAL_DISPATCH1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> internalDispatchService.findById(INTERNAL_DISPATCH1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> internalDispatchService.deleteById(NOT_FOUND));
    }
}