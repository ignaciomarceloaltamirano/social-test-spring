package com.social.test.controllers;

import com.social.test.dtos.request.UpdateUserRequestDto;
import com.social.test.entities.User;
import com.social.test.repositories.UserRepository;
import com.social.test.services.IUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @Transactional
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Long userId,
            @RequestPart("user") UpdateUserRequestDto updateUserRequestDto,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(userService.updateUser(userId, updateUserRequestDto, file));
    }
}
