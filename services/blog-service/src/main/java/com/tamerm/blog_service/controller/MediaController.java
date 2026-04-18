package com.tamerm.blog_service.controller;

import com.tamerm.blog_service.dto.MediaDTO;
import com.tamerm.blog_service.security.AuthenticatedUser;
import com.tamerm.blog_service.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/posts/{postId}/media")
    public ResponseEntity<List<MediaDTO>> uploadMedia(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal AuthenticatedUser principal) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.addMedia(postId, file, principal));
    }

    @DeleteMapping("/posts/{postId}/media/{groupId}")
    public ResponseEntity<Void> removeMedia(
            @PathVariable Long postId,
            @PathVariable String groupId,
            @AuthenticationPrincipal AuthenticatedUser principal) throws IOException {
        mediaService.removeMedia(postId, groupId, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/media/{mediaId}")
    public ResponseEntity<Resource> downloadMedia(@PathVariable Long mediaId) throws IOException {
        Path filePath = mediaService.resolveFilePath(mediaId);
        Resource resource = new UrlResource(filePath.toUri());
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName() + "\"")
                .body(resource);
    }
}
