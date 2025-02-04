package com.tamerm.blog_app.dto;

import lombok.Data;

/**
 * Data Transfer Object for users.
 */
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String displayName;
}