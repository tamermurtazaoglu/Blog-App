package com.tamerm.blog_app.unit.controller;

import com.tamerm.blog_app.controller.UserController;
import com.tamerm.blog_app.dto.UserDTO;
import com.tamerm.blog_app.request.CreateUserRequest;
import com.tamerm.blog_app.request.LoginRequest;
import com.tamerm.blog_app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Unit tests for {@link UserController}.
 */
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that a user is created successfully.
     */
    @Test
    void createUser_ShouldReturnCreatedUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setDisplayName("Test User");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setDisplayName("Test User");

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createUser(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(userDTO, response.getBody());
    }

    /**
     * Tests that a JWT token is returned on successful login.
     */
    @Test
    void login_ShouldReturnJwtToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        String jwtToken = "jwt-token";

        when(userService.login(any(LoginRequest.class))).thenReturn(jwtToken);

        ResponseEntity<String> response = userController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jwtToken, response.getBody());
    }

    /**
     * Tests that the security context is cleared on logout.
     */
    @Test
    void logout_ShouldReturnNoContent() {
        doNothing().when(userService).logout();

        ResponseEntity<Void> response = userController.logout();

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).logout();
    }
}