package netology.diplom.filestorage.testcontainer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final PostgreSQLContainer dbContainer = (PostgreSQLContainer) new PostgreSQLContainer(
            DockerImageName.parse("postgres"))
            .withDatabaseName("template")
            .withUsername("template")
            .withPassword("template")
            .withReuse(true);

    static {
        dbContainer.start();
    }

    @Override
    public void initialize(final ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + dbContainer.getJdbcUrl(),
                "spring.datasource.username=" + dbContainer.getUsername(),
                "spring.datasource.password=" + dbContainer.getPassword()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}
