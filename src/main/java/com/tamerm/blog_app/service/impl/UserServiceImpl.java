package com.tamerm.blog_app.service.impl;

import com.tamerm.blog_app.dto.UserDTO;
import com.tamerm.blog_app.exception.InvalidCredentialsException;
import com.tamerm.blog_app.exception.UserAlreadyLoggedInException;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.UserRepository;
import com.tamerm.blog_app.request.CreateUserRequest;
import com.tamerm.blog_app.request.LoginRequest;
import com.tamerm.blog_app.security.JWTService;
import com.tamerm.blog_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing users.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final JWTService jwtService;

    /**
     * Creates a new user with the given request details.
     *
     * @param request the request containing user details
     * @return the created UserDTO
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
     * Logs in a user with the given request details.
     *
     * @param request the request containing login details
     * @return the generated JWT token
     * @throws UserAlreadyLoggedInException if the user is already logged in
     * @throws InvalidCredentialsException  if the provided credentials are invalid
     */
    @Override
    public String login(LoginRequest request) {
        if (jwtService.hasActiveToken()) {
            throw new UserAlreadyLoggedInException("User is already logged in with an active token");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            return jwtService.generateToken(user);
        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    /**
     * Logs out the currently authenticated user.
     *
     * @throws RuntimeException if no user is currently logged in
     */
    @Override
    public void logout() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new RuntimeException("No user is currently logged in.");
        }
        SecurityContextHolder.clearContext();
    }

    /**
     * Loads a user by their username.
     *
     * @param username the username of the user to load
     * @return the UserDetails of the loaded user
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}