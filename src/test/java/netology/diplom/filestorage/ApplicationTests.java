package netology.diplom.filestorage;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import netology.diplom.filestorage.testcontainer.PostgresInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = PostgresInitializer.class)
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}
