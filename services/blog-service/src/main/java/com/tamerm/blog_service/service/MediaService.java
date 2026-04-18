package com.tamerm.blog_service.service;

import com.tamerm.blog_service.dto.MediaDTO;
import com.tamerm.blog_service.security.AuthenticatedUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MediaService {
    List<MediaDTO> addMedia(Long postId, MultipartFile file, AuthenticatedUser principal) throws IOException;
    void removeMedia(Long postId, String groupId, AuthenticatedUser principal) throws IOException;
    Path resolveFilePath(Long mediaId);
}
