package com.tamerm.blog_service.dto;

import com.tamerm.blog_service.model.MediaResolution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
