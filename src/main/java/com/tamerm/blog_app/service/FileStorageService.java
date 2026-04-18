package com.tamerm.blog_app.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {

    /**
     * Stores a file under the given sub-path relative to the storage root.
     *
     * @param file    the multipart file to store
     * @param subPath relative path including file name (e.g. "groupId/ORIGINAL_filename.jpg")
     * @return the absolute path where the file was stored
     */
    Path store(MultipartFile file, String subPath) throws IOException;

    /**
     * Stores raw bytes under the given sub-path.
     */
    Path storeBytes(byte[] bytes, String subPath) throws IOException;

    /**
     * Resolves the absolute path for a given sub-path.
     */
    Path resolve(String subPath);

    /**
     * Deletes the file at the given sub-path.
     */
    void delete(String subPath) throws IOException;
}
