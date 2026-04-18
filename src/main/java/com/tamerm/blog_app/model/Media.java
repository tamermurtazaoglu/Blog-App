package com.tamerm.blog_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a media file (image or video) attached to a blog post.
 * Image uploads produce one record per resolution variant (ORIGINAL, LARGE, MEDIUM, SMALL).
 * All variants of the same upload share the same groupId.
 */
@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String contentType;

    private long fileSize;

    @Column(nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaResolution resolution;

    /**
     * Shared identifier for all resolution variants of the same upload.
     * Used to retrieve or delete all variants together.
     */
    @Column(nullable = false)
    private String groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
