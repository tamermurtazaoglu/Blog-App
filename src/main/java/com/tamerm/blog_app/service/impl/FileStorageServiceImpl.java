package com.tamerm.blog_app.service.impl;

import com.tamerm.blog_app.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path storageRoot;

    public FileStorageServiceImpl(@Value("${app.media.storage-path}") String storagePath) {
        this.storageRoot = Paths.get(storagePath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(storageRoot);
        log.info("Media storage root: {}", storageRoot);
    }

    @Override
    public Path store(MultipartFile file, String subPath) throws IOException {
        Path target = storageRoot.resolve(subPath).normalize();
        Files.createDirectories(target.getParent());
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Stored file: {}", target);
        return target;
    }

    @Override
    public Path storeBytes(byte[] bytes, String subPath) throws IOException {
        Path target = storageRoot.resolve(subPath).normalize();
        Files.createDirectories(target.getParent());
        Files.write(target, bytes);
        log.debug("Stored resized file: {}", target);
        return target;
    }

    @Override
    public Path resolve(String subPath) {
        return storageRoot.resolve(subPath).normalize();
    }

    @Override
    public void delete(String subPath) throws IOException {
        Path target = storageRoot.resolve(subPath).normalize();
        Files.deleteIfExists(target);
        log.debug("Deleted file: {}", target);
    }
}
