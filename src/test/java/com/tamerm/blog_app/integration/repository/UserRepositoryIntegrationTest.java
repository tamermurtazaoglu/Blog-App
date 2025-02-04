package com.tamerm.blog_app.integration.repository;

import com.tamerm.blog_app.TestContainersConfig;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = TestContainersConfig.class)
@Transactional
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = User.builder()
                .username("testuser")
                .password("$2a$10$7QJ8K1Q8Q1Q8Q1Q8Q1Q8QO1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q")
                .displayName("Test User")
                .build();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getPassword()).isEqualTo("$2a$10$7QJ8K1Q8Q1Q8Q1Q8Q1Q8QO1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q");
        assertThat(savedUser.getDisplayName()).isEqualTo("Test User");
    }

    @Test
    void testFindUserByUsername() {
        User user = User.builder()
                .username("testuser")
                .password("$2a$10$7QJ8K1Q8Q1Q8Q1Q8Q1Q8QO1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q1Q8Q")
                .displayName("Test User")
                .build();

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }
}