package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.repository.ExternalDispatchRepository;
import com.igar15.filecabinet.service.ExternalDispatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExternalDispatchServiceImplTest extends AbstractServiceTest {

    @Autowired
    private ExternalDispatchService externalDispatchService;

    @Test
    void create() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findAllByDocumentId() {
        List<ExternalDispatch> allByDocumentId = externalDispatchService.findAllByDocumentId(1009);
        allByDocumentId.forEach(System.out::println);
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}