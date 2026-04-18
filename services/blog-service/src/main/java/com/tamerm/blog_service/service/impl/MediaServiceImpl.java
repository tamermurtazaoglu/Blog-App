package com.tamerm.blog_service.service.impl;

import com.tamerm.blog_service.dto.MediaDTO;
import com.tamerm.blog_service.exception.ResourceNotFoundException;
import com.tamerm.blog_service.exception.UnauthorizedException;
import com.tamerm.blog_service.model.Media;
import com.tamerm.blog_service.model.MediaResolution;
import com.tamerm.blog_service.model.Post;
import com.tamerm.blog_service.repository.MediaRepository;
import com.tamerm.blog_service.repository.PostRepository;
import com.tamerm.blog_service.security.AuthenticatedUser;
import com.tamerm.blog_service.service.FileStorageService;
import com.tamerm.blog_service.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {

    private static final int[] IMAGE_WIDTHS = {1280, 640, 320};
    private static final MediaResolution[] IMAGE_RESOLUTIONS = {
            MediaResolution.LARGE, MediaResolution.MEDIUM, MediaResolution.SMALL
    };

    private final MediaRepository mediaRepository;
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    @Override
    public List<MediaDTO> addMedia(Long postId, MultipartFile file, AuthenticatedUser principal) throws IOException {
        Post post = findPostAndVerifyOwner(postId, principal.getUserId());

        String groupId = UUID.randomUUID().toString();
        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        List<Media> savedMedia = new ArrayList<>();

        String originalSubPath = groupId + "/ORIGINAL_" + originalFileName;
        fileStorageService.store(file, originalSubPath);
        savedMedia.add(mediaRepository.save(Media.builder()
                .fileName(originalFileName)
                .contentType(contentType)
                .fileSize(file.getSize())
                .filePath(originalSubPath)
                .resolution(MediaResolution.ORIGINAL)
                .groupId(groupId)
                .post(post)
                .build()));

        if (contentType.startsWith("image/")) {
            byte[] originalBytes = file.getBytes();
            for (int i = 0; i < IMAGE_WIDTHS.length; i++) {
                int width = IMAGE_WIDTHS[i];
                MediaResolution resolution = IMAGE_RESOLUTIONS[i];
                String format = contentType.equals("image/png") ? "png" : "jpg";

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Thumbnails.of(new java.io.ByteArrayInputStream(originalBytes))
                        .width(width)
                        .outputFormat(format)
                        .toOutputStream(baos);

                String subPath = groupId + "/" + resolution.name() + "_" + originalFileName;
                fileStorageService.storeBytes(baos.toByteArray(), subPath);

                savedMedia.add(mediaRepository.save(Media.builder()
                        .fileName(resolution.name().toLowerCase() + "_" + originalFileName)
                        .contentType(contentType)
                        .fileSize(baos.size())
                        .filePath(subPath)
                        .resolution(resolution)
                        .groupId(groupId)
                        .post(post)
                        .build()));
            }
        }

        log.info("Added {} media variant(s) to post {}", savedMedia.size(), postId);
        return savedMedia.stream().map(this::toDTO).toList();
    }

    @Transactional
    @Override
    public void removeMedia(Long postId, String groupId, AuthenticatedUser principal) throws IOException {
        findPostAndVerifyOwner(postId, principal.getUserId());

        List<Media> variants = mediaRepository.findAllByGroupId(groupId);
        if (variants.isEmpty()) {
            throw new ResourceNotFoundException("Media not found with groupId: " + groupId);
        }

        for (Media media : variants) {
            fileStorageService.delete(media.getFilePath());
        }
        mediaRepository.deleteAll(variants);
        log.info("Removed {} media variant(s) (groupId={}) from post {}", variants.size(), groupId, postId);
    }

    @Override
    public Path resolveFilePath(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + mediaId));
        return fileStorageService.resolve(media.getFilePath());
    }

    private Post findPostAndVerifyOwner(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        if (!post.getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to modify media on this post");
        }
        return post;
    }

    private MediaDTO toDTO(Media media) {
        return MediaDTO.builder()
                .id(media.getId())
                .fileName(media.getFileName())
                .contentType(media.getContentType())
                .fileSize(media.getFileSize())
                .resolution(media.getResolution())
                .groupId(media.getGroupId())
                .downloadUrl("/media/" + media.getId())
                .build();
    }
}
