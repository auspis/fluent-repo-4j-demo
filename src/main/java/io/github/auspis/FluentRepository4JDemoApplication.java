package io.github.auspis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;

import io.github.auspis.domain.entity.User;
import io.github.auspis.fluentrepo4j.config.EnableFluentRepositories;
import io.github.auspis.fluentsql4j.dsl.DSL;
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
    CommandLineRunner demo(UserRepository userRepository, DSL dsl, DataSource dataSource) {
        return args -> {
            createUsersTable(dataSource, dsl);

            System.out.println("\n=== 1) COUNT ===");
            System.out.println("  count=" + userRepository.count());

            System.out.println("\n=== 2) INSERT ===");
            User inserted = new User("John Wick", "john.wick@example.com", 31);
            inserted.setId(ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE));
            inserted = userRepository.save(inserted);
            System.out.println("  Inserted: " + inserted);

            System.out.println("\n=== 3) FIND after insert ===");
            userRepository.findAll().forEach(user -> System.out.println("  " + user));

            System.out.println("\n=== 4) UPDATE ===");
            inserted.setAge(32);
            inserted.setName("John Wick Updated");
            User updated = userRepository.save(inserted);
            System.out.println("  Updated: " + updated);

            System.out.println("\n=== 5) FIND after update ===");
            userRepository.findAll().forEach(user -> System.out.println("  " + user));

            System.out.println("\n=== 6) DELETE ===");
            userRepository.deleteById(updated.getId());
            System.out.println("  Deleted id=" + updated.getId());

            System.out.println("\n=== 7) COUNT final ===");
            System.out.println("  count=" + userRepository.count());
        };
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

}
