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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final JWTService jwtService;

    /**
     * Creates a new user with the given request details.
     *
     * @param request the request containing user details
     * @return the created UserDTO
     */
    @Transactional
    @Override
    public UserDTO createUser(CreateUserRequest request) {
        log.debug("Creating user with username: {}", request.getUsername());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName());
        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setUsername(savedUser.getUsername());
        userDTO.setDisplayName(savedUser.getDisplayName());
        log.info("User created successfully with id: {}", savedUser.getId());
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
    @Transactional
    @Override
    public String login(LoginRequest request) {
        log.debug("Authenticating user with username: {}", request.getUsername());
        if (jwtService.hasActiveToken()) {
            log.error("User is already logged in with an active token");
            throw new UserAlreadyLoggedInException("User is already logged in with an active token");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            log.info("User authenticated successfully with username: {}", request.getUsername());

            // Manually set the authentication in the SecurityContextHolder
            UserDetails userDetails = loadUserByUsername(request.getUsername());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return token;
        } else {
            log.error("Invalid username or password for username: {}", request.getUsername());
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
        log.debug("Logging out current user");
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.error("No user is currently logged in.");
            throw new RuntimeException("No user is currently logged in.");
        }
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
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
        log.debug("Loading user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}