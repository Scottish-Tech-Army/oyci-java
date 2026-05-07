package org.scottishtecharmy.oyci;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = "spring.sql.init.mode=never")
@Import(TestMailConfig.class)
class OyciJavaApplicationTests {

    @Test
    void contextLoads() {
    }
}

