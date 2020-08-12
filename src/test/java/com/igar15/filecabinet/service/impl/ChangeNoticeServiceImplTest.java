package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.ChangeNoticeTestData;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.ChangeNoticeService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

import static com.igar15.filecabinet.ChangeNoticeTestData.*;
import static org.junit.jupiter.api.Assertions.*;

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
            validateRootCause(ConstraintViolationException.class, () -> changeNoticeService.create(changeNotice));
        });
    }

    @Test
    void findById() {
        ChangeNotice found = changeNoticeService.findById(CHANGE_NOTICE1_ID);
        Assertions.assertEquals(CHANGE_NOTICE1, found);
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> changeNoticeService.findById(NOT_FOUND_ID));
    }

    @Test
    void findByName() {
        ChangeNotice found = changeNoticeService.findByName(CHANGE_NOTICE1_NAME);
        Assertions.assertEquals(CHANGE_NOTICE1, found);
    }

    @Test
    void findByNameNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> changeNoticeService.findByName(NOT_FOUND_NAME));
    }

    @Test
    void findByIdWithDocuments() {
        ChangeNotice found = changeNoticeService.findById(CHANGE_NOTICE1_ID + 2);
        Map<Document, Integer> documents = found.getDocuments();
        Assertions.assertEquals(getWithDocuments(), found);
    }

//    @Test
//    void findAll() {
//        List<ChangeNotice> changeNotices = changeNoticeService.findAll();
//        Assertions.assertEquals(CHANGE_NOTICES, changeNotices);
//    }

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