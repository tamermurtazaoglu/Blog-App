package com.tamerm.blog_app.model;

/**
 * Represents the display resolution variant of a media file.
 * Image uploads are stored in all four variants; videos are stored as ORIGINAL only.
 */
public enum MediaResolution {
    ORIGINAL,
    LARGE,   // 1280px wide
    MEDIUM,  // 640px wide
    SMALL    // 320px wide
}
