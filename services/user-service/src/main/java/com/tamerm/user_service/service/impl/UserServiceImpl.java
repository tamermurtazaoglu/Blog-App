package com.tamerm.user_service.service.impl;

import com.tamerm.user_service.dto.UserDTO;
import com.tamerm.user_service.exception.ResourceNotFoundException;
import com.tamerm.user_service.kafka.UserDeletedEvent;
import com.tamerm.user_service.kafka.UserEventProducer;
import com.tamerm.user_service.model.User;
import com.tamerm.user_service.repository.UserRepository;
import com.tamerm.user_service.request.CreateUserRequest;
import com.tamerm.user_service.request.LoginRequest;
import com.tamerm.user_service.security.JWTService;
import com.tamerm.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    @Override
    public UserDTO createUser(CreateUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .build();
        User saved = userRepository.save(user);
        log.info("User created: {}", saved.getUsername());
        return new UserDTO(saved.getId(), saved.getUsername(), saved.getDisplayName());
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        UserDetails userDetails = loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);
        log.info("User logged in: {}", request.getUsername());
        return token;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        userRepository.delete(user);
        userEventProducer.publishUserDeleted(new UserDeletedEvent(user.getId(), user.getUsername()));
        log.info("User deleted and event published: {}", userId);
    }
}
