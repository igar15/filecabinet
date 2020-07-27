package com.igar15.filecabinet.service;

import com.igar15.filecabinet.TestTimeExtension;
import com.igar15.filecabinet.entity.Developer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;

@SpringBootTest
@Sql(scripts = "classpath:db_scripts/populate_db.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TestTimeExtension.class)
class DeveloperServiceImplTest {



    @Autowired
    private DeveloperService service;

    @Test
    void findAll() {
        List<Developer> developers = service.findAll();
        Assertions.assertEquals(6, developers.size());
    }

    @Test
    void findById() {
        Assertions.assertEquals("KTK-40", service.findById(1000).getName());
    }

    @Test
    void save() {

    }

    @Test
    void deleteById() {
        service.deleteById(1000);
        Assertions.assertThrows(RuntimeException.class, () -> service.findById(1000));
    }
}