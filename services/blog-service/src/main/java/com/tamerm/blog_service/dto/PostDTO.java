package com.tamerm.blog_service.dto;

import com.tamerm.blog_service.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String text;
    private Long userId;
    private Set<Tag> tags;
    private List<MediaDTO> mediaFiles;
}
