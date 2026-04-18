package com.tamerm.blog_app.service;

import com.tamerm.blog_app.dto.MediaDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MediaService {

    /**
     * Attaches a media file to a post.
     * Images are stored in ORIGINAL, LARGE, MEDIUM and SMALL resolution variants.
     * Videos are stored as ORIGINAL only.
     *
     * @return list of created MediaDTO records (one per resolution variant)
     */
    List<MediaDTO> addMedia(Long postId, MultipartFile file, UserDetails userDetails) throws IOException;

    /**
     * Removes all resolution variants of a media upload identified by groupId.
     */
    void removeMedia(Long postId, String groupId, UserDetails userDetails) throws IOException;

    /**
     * Resolves the filesystem path for a specific media record.
     */
    Path resolveFilePath(Long mediaId);
}
