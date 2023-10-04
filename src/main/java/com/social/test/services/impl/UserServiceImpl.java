package com.social.test.services.impl;

import com.social.test.dtos.request.UpdateUserRequestDto;
import com.social.test.entities.User;
import com.social.test.repositories.UserRepository;
import com.social.test.services.IFileUploadService;
import com.social.test.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final IFileUploadService fileUploadService;

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    public User updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        if (updateUserRequestDto.getUsername() != null &&
                !user.getUsername().equals(updateUserRequestDto.getUsername())) {
            user.setUsername(updateUserRequestDto.getUsername());
        }

        if (file != null) {
            String imageUrl = fileUploadService.uploadFile(file);
            user.setImageUrl(imageUrl);
        }
        return user;
    }
}
