package com.tamerm.blog_app.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request object for updating an existing post.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {

    @NotNull(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Content is required")
    @Size(min = 1, message = "Content cannot be empty")
    private String text;

    private List<String> tags;
}