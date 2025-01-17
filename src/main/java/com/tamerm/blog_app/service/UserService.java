package com.tamerm.blog_app.service;

import com.tamerm.blog_app.dto.UserDTO;
import com.tamerm.blog_app.request.CreateUserRequest;
import com.tamerm.blog_app.request.LoginRequest;

public interface UserService {
    UserDTO createUser(CreateUserRequest request);
    String login(LoginRequest request);
    void logout();
}