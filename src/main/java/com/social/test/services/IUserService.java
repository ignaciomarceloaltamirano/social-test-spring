package com.social.test.services;

import com.social.test.dtos.request.UpdateUserRequestDto;
import com.social.test.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserService {
    User getUser(Long userId);
    User updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto, MultipartFile file) throws IOException;
}
