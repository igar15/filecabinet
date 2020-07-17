package com.igar15.filecabinet.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:db_scripts/populate_db.sql", config = @SqlConfig(encoding = "UTF-8"))
class DocumentServiceImplTest {

    @Autowired
    private DocumentService service;

    @Test
    void findAll() {
        Assertions.assertEquals(6, service.findAll().size());
    }

    @Test
    void findById() {
        Assertions.assertEquals("БА6.151.128", service.findById(1006).getDecimalNumber());
    }

    @Test
    void save() {

    }

    @Test
    void deleteById() {
        service.deleteById(1006);
        Assertions.assertThrows(RuntimeException.class, () -> service.findById(1006));
    }
}