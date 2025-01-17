package com.tamerm.blog_app.unit.service.impl;

import com.tamerm.blog_app.dto.UserDTO;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.UserRepository;
import com.tamerm.blog_app.request.CreateUserRequest;
import com.tamerm.blog_app.request.LoginRequest;
import com.tamerm.blog_app.security.JWTService;
import com.tamerm.blog_app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImpl}.
 */
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private CreateUserRequest createUserRequest;
    private LoginRequest loginRequest;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setDisplayName("Test User");

        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setPassword("password");
        createUserRequest.setDisplayName("Test User");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
    }

    /**
     * Tests that a user is created successfully.
     */
    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(createUserRequest);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Tests that a JWT token is returned on successful login.
     */
    @Test
    void login_ShouldReturnJwtToken() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        String result = userService.login(loginRequest);

        assertNotNull(result);
        assertEquals("jwt-token", result);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    /**
     * Tests that an exception is thrown when login credentials are invalid.
     */
    @Test
    void login_ShouldThrowException_WhenInvalidCredentials() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        loginRequest.setPassword("wrongpassword");

        assertThrows(RuntimeException.class, () -> userService.login(loginRequest));
    }

    /**
     * Tests that the security context is cleared on logout.
     */
    @Test
    void logout_ShouldClearSecurityContext() {
        userService.logout();

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}