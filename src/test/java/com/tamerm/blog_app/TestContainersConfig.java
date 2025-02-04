package com.tamerm.blog_app;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class TestContainersConfig {

    @Bean
    public MySQLContainer<?> mySQLContainer() {
        MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.2.0")
                .withDatabaseName("blog_app")
                .withUsername("tamerm")
                .withPassword("tamerm");
        mysql.start();
        return mysql;
    }
}