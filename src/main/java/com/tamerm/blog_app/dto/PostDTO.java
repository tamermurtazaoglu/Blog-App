package com.tamerm.blog_app.dto;

import com.tamerm.blog_app.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * Data Transfer Object for posts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String text;
    private Set<Tag> tags;
    private List<MediaDTO> mediaFiles;
}