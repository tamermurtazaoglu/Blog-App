package com.tamerm.blog_app.dto;

import com.tamerm.blog_app.model.MediaResolution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for media files attached to a post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaDTO {
    private Long id;
    private String fileName;
    private String contentType;
    private long fileSize;
    private MediaResolution resolution;
    private String groupId;
    private String downloadUrl;
}
