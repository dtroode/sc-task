package com.example.scdemo.storageservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    // Directory where to save bookfiles.
    private final Path root = Paths.get("/tmp");

    @Override
    public void init() {
        try {
            // Check if directory exists and if not — create it.
            if (!Files.isDirectory(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't create folder. Error: " + e);
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            // Save Multipart file by getting it's Input Stream.
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Can't store file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            // Get file by it's name.
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                // If everything is ok with file — return file.
                return resource;
            } else {
                // If something isn't ok — throw runtime exception.
                throw new RuntimeException("Could not read file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
