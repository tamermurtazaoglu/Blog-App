package com.tamerm.blog_app.service.impl;

import com.tamerm.blog_app.dto.UserDTO;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.UserRepository;
import com.tamerm.blog_app.request.CreateUserRequest;
import com.tamerm.blog_app.request.LoginRequest;
import com.tamerm.blog_app.security.JWTService;
import com.tamerm.blog_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTService jwtService;

    /**
     * Creates a new user.
     *
     * @param request the request object containing user details
     * @return the created user
     */
    @Override
    public UserDTO createUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName());
        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setUsername(savedUser.getUsername());
        userDTO.setDisplayName(savedUser.getDisplayName());
        return userDTO;
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the request object containing login details
     * @return the JWT token
     * @throws RuntimeException if the username or password is invalid
     */
    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            return jwtService.generateToken(user);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    /**
     * Logs out the current user.
     */
    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}