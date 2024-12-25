package com.tamerm.blog_app.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request object for creating a new post.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {

    @NotNull(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Content is required")
    @Size(min = 1, message = "Content cannot be empty")
    private String text;

    private List<String> tags;
}