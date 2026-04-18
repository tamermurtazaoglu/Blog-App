package com.tamerm.blog_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    Path store(MultipartFile file, String subPath) throws IOException;
    Path storeBytes(byte[] bytes, String subPath) throws IOException;
    Path resolve(String subPath);
    void delete(String subPath) throws IOException;
}
