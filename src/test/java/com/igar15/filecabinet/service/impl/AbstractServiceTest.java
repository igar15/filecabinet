package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.TestTimeExtension;
import com.igar15.filecabinet.util.validation.ValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@SpringBootTest
@Sql(scripts = "classpath:db_scripts/populate_db.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TestTimeExtension.class)
public abstract class AbstractServiceTest {

    public <T extends Throwable> void validateRootCause(Class<T> rootExceptionClass, Runnable runnable) {
        Assertions.assertThrows(rootExceptionClass, () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw ValidationUtil.getRootCause(e);
            }
        });
    }

}
