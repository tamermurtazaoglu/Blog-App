package com.tamerm.blog_app.integration.service.impl;

import com.tamerm.blog_app.dto.UserDTO;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.UserRepository;
import com.tamerm.blog_app.request.CreateUserRequest;
import com.tamerm.blog_app.request.LoginRequest;
import com.tamerm.blog_app.security.JWTService;
import com.tamerm.blog_app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Test
    @Transactional
    void testCreateUser() {
        String uniqueUsername = "testuser" + System.currentTimeMillis();
        CreateUserRequest request = new CreateUserRequest(uniqueUsername, "password", "Test User");
        UserDTO createdUser = userService.createUser(request);

        assertNotNull(createdUser);
        assertEquals(uniqueUsername, createdUser.getUsername());
        assertEquals("Test User", createdUser.getDisplayName());

        User user = userRepository.findByUsername(uniqueUsername).orElse(null);
        assertNotNull(user);
        assertTrue(new BCryptPasswordEncoder().matches("password", user.getPassword()));
    }

    @Test
    @Transactional
    void testLogin() {
        CreateUserRequest createUserRequest = new CreateUserRequest("testuser", "password", "Test User");
        userService.createUser(createUserRequest);

        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        String jwt = userService.login(loginRequest);

        assertNotNull(jwt);
        assertTrue(jwtService.validateToken(jwt));
    }

    @Test
    @Transactional
    void testLogout() {
        CreateUserRequest createUserRequest = new CreateUserRequest("testuser", "password", "Test User");
        userService.createUser(createUserRequest);

        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        String jwt = userService.login(loginRequest);

        userService.logout();
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}