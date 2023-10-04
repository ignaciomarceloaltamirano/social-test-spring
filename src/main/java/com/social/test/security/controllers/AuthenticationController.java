package com.social.test.security.controllers;

import com.social.test.dtos.request.LoginUserRequestDto;
import com.social.test.dtos.request.RegisterUserDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.security.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> registerUser(
            @RequestPart(value = "user") RegisterUserDto registerUserDto,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(authenticationService.register(registerUserDto, file));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody LoginUserRequestDto loginUserRequestDto
    ) {
        return authenticationService.login(loginUserRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return authenticationService.logout();
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return authenticationService.refreshToken(request);
    }
}
