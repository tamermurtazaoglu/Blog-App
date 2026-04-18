package com.tamerm.user_service.service;

import com.tamerm.user_service.dto.UserDTO;
import com.tamerm.user_service.request.CreateUserRequest;
import com.tamerm.user_service.request.LoginRequest;

public interface UserService {
    UserDTO createUser(CreateUserRequest request);
    String login(LoginRequest request);
    void logout();
    void deleteUser(Long userId);
}
