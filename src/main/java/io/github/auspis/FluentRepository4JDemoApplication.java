package io.github.auspis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

import io.github.auspis.domain.entity.Plant;
import io.github.auspis.domain.entity.User;
import io.github.auspis.fluentrepo4j.config.EnableFluentRepositories;
import io.github.auspis.fluentrepo4j.functional.write.WriteResult;
import io.github.auspis.fluentsql4j.dsl.DSL;
import io.github.auspis.repository.PlantRepository;
import io.github.auspis.repository.UserRepository;

@Configuration(proxyBeanMethods = false)
@EnableFluentRepositories(basePackageClasses = UserRepository.class)
@SpringBootApplication
public class FluentRepository4JDemoApplication{


    @SuppressWarnings("resource")
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("myapp")
            .withUsername("demo")
            .withPassword("demo");

    public static void main( String[] args ) {
        MYSQL.start();

        Runtime.getRuntime().addShutdownHook(new Thread(MYSQL::stop));

        SpringApplication app = new SpringApplication(FluentRepository4JDemoApplication.class);
        app.setDefaultProperties(Map.of(
                "spring.datasource.url", MYSQL.getJdbcUrl(),
                "spring.datasource.username", MYSQL.getUsername(),
                "spring.datasource.password", MYSQL.getPassword(),
                "spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver"
        ));
        app.run(args);
    }

    @Bean
    CommandLineRunner demo(UserRepository userRepository, PlantRepository plantRepository, DSL dsl, DataSource dataSource) {
        return args -> {
            createUsersTable(dataSource, dsl);
            createPlantsTable(dataSource, dsl);

            seedUsers(userRepository);
            seedPlants(plantRepository);
        };
    }

    private static void seedUsers(UserRepository userRepository) {
        User alice = new User("Alice Rossi", "alice.rossi@example.com", 28);
        alice.setId(1L);
        userRepository.save(alice);

        User bob = new User("Bob Marley", "bob.marley@example.com", 35);
        bob.setId(2L);
        userRepository.save(bob);

        User carol = new User("Carol White", "carol.white@example.com", 42);
        carol.setId(3L);
        userRepository.save(carol);
    }

    private static void seedPlants(PlantRepository plantRepository) {
        Plant rose = new Plant("Rose", "Rosa damascena", 3);
        rose.setId(1L);
        assertWriteSuccess(plantRepository.save(rose), "Failed to seed plant Rose");

        Plant fern = new Plant("Fern", "Polypodiopsida", 2);
        fern.setId(2L);
        assertWriteSuccess(plantRepository.save(fern), "Failed to seed plant Fern");

        Plant cactus = new Plant("Cactus", "Cactaceae", 14);
        cactus.setId(3L);
        assertWriteSuccess(plantRepository.save(cactus), "Failed to seed plant Cactus");
    }

    private static void assertWriteSuccess(WriteResult<?> result, String message) {
        if (result instanceof WriteResult.Error<?> error) {
            throw new IllegalStateException(error.message() == null ? message : error.message());
        }
    }

    private static void createUsersTable(DataSource dataSource, DSL dsl) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            dsl.createTable("users")
                    .columnIntegerPrimaryKey("id")
                    .column("name").varchar(255)
                    .column("email_address").varchar(255)
                    .column("age").integer()
                    .build(connection)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createPlantsTable(DataSource dataSource, DSL dsl) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            dsl.createTable("plants")
                    .columnIntegerPrimaryKey("id")
                    .column("name").varchar(255)
                    .column("scientific_name").varchar(255)
                    .column("watering_frequency_days").integer()
                    .build(connection)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
