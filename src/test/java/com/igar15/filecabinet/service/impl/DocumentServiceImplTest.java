package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.DocumentTestData;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.service.DocumentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.igar15.filecabinet.DocumentTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DocumentService documentService;

    @Test
    void create() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByIdWithChangeNotices() {
    }

    @Test
    void findByDecimalNumber() {
    }

    @Test
    void findAll() {
        List<Document> documents = documentService.findAll();
        Assertions.assertEquals(DOCUMENTS, documents);

    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}