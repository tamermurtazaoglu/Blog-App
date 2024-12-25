package com.tamerm.blog_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for summarizing posts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryDTO {
    private String title;
    private String text;
}