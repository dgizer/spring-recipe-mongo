package com.springframework.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImage(String id, MultipartFile file);
}
