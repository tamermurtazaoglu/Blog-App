package com.tamerm.blog_app.controller;

import com.tamerm.blog_app.dto.UserDTO;
import com.tamerm.blog_app.request.CreateUserRequest;
import com.tamerm.blog_app.request.LoginRequest;
import com.tamerm.blog_app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    /**
     * Create a new user.
     *
     * @param request the request object containing user details
     * @return the created user
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Creates a new user with the given details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody @Parameter(description = "Request object containing user details") CreateUserRequest request) {
        UserDTO createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Authenticate a user and return a JWT token.
     *
     * @param request the request object containing login details
     * @return the JWT token
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<String> login(
            @Valid @RequestBody @Parameter(description = "Request object containing login details") LoginRequest request) {
        String jwt = userService.login(request);
        return ResponseEntity.ok(jwt);
    }

    /**
     * Logout the current user.
     *
     * @return a response entity with no content
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout the current user", description = "Logs out the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User logged out successfully")
    })
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.noContent().build();
    }
}