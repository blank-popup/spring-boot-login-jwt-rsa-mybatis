package com.example.loginJwtRSA;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test_server")
@TestPropertySource(properties="logging.config=classpath:logback-spring.xml")
class LoginJwtRsaApplicationTests {

    @Test
    void contextLoads() {
    }

}
