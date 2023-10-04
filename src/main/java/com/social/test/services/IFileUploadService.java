package com.social.test.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileUploadService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
