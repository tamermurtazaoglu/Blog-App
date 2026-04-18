package com.tamerm.blog_app.controller;

import com.tamerm.blog_app.dto.MediaDTO;
import com.tamerm.blog_app.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Media Controller", description = "APIs for managing post media (images and videos)")
@SecurityRequirement(name = "bearerAuth")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/posts/{postId}/media")
    @Operation(summary = "Upload media to a post",
            description = "Images are stored in ORIGINAL, LARGE, MEDIUM and SMALL variants. Videos are stored as-is.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Media uploaded successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<List<MediaDTO>> uploadMedia(
            @PathVariable Long postId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        List<MediaDTO> result = mediaService.addMedia(postId, file, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/posts/{postId}/media/{groupId}")
    @Operation(summary = "Remove media from a post",
            description = "Deletes all resolution variants of the specified media upload.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Media removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Post or media not found")
    })
    public ResponseEntity<Void> removeMedia(
            @PathVariable Long postId,
            @PathVariable String groupId,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        mediaService.removeMedia(postId, groupId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/media/{mediaId}")
    @Operation(summary = "Download a media file", description = "Serves the media file by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File served successfully"),
            @ApiResponse(responseCode = "404", description = "Media not found")
    })
    public ResponseEntity<Resource> downloadMedia(@PathVariable Long mediaId) throws IOException {
        Path filePath = mediaService.resolveFilePath(mediaId);
        Resource resource = new UrlResource(filePath.toUri());
        String contentType = java.nio.file.Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName() + "\"")
                .body(resource);
    }
}
