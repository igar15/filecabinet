package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.DocumentTestData;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.repository.DocumentRepository;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.igar15.filecabinet.DocumentTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;

//    @Test
//    void find() {
//        List<Document> byApplicabilities_decimalNumber = documentRepository.findByApplicabilities_DecimalNumber("БА6.151.128");
//        byApplicabilities_decimalNumber.forEach(d -> System.out.println(d.getDecimalNumber()));
//    }

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
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> documentService.findById(NOT_FOUND_ID));
    }

    @Test
    void findByIdWithChangeNotices() {
        Document found = documentService.findByIdWithChangeNotices(DOCUMENT1_ID);
        Assertions.assertEquals(getWithChangeNotices(), found);
    }

    @Test
    void findByDecimalNumber() {
        Document found = documentService.findByDecimalNumber(DOCUMENT1_DECIMAL_NUMBER);
        Assertions.assertEquals(DOCUMENT1, found);
    }

    @Test
    void findByDecimalNumberNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> documentService.findByDecimalNumber(NOT_FOUND_DECIMAL_NUMBER));
    }

//    @Test
//    void findAll() {
//        List<Document> documents = documentService.findAll();
//        Assertions.assertEquals(DOCUMENTS, documents);
//
//    }

    @Test
    void update() {
        Document updated = getUpdated();
        documentService.update(updated);
        Assertions.assertEquals(updated, documentService.findById(DOCUMENT1_ID));
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