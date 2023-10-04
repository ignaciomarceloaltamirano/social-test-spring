package com.social.test.security.services;

import com.social.test.dtos.response.LoginUserResponseDto;
import com.social.test.dtos.request.LoginUserRequestDto;
import com.social.test.dtos.request.RegisterUserDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.entities.RefreshToken;
import com.social.test.entities.Role;
import com.social.test.entities.User;
import com.social.test.enumerations.ERole;
import com.social.test.exceptions.ResourceNotFoundException;
import com.social.test.exceptions.TokenRefreshException;
import com.social.test.repositories.RoleRepository;
import com.social.test.repositories.UserRepository;
import com.social.test.services.IFileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final IFileUploadService fileUploadService;

    public MessageDto register(RegisterUserDto registerUserDto, MultipartFile file) throws IOException {
        if (userRepository.existsByUsername(registerUserDto.getUsername())) {
            throw new RuntimeException("Error: Username is already in use");
        }

        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new RuntimeException("Error: Email is already in use");
        }

        User user = User.builder()
                .username(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .build();

        if(!roleRepository.existsByName(ERole.ROLE_USER)){
            Role roleUser=new Role();
            roleUser.setName(ERole.ROLE_USER);
            roleRepository.save(roleUser);
        }

        if(file!=null){
            String imageUrl=fileUploadService.uploadFile(file);
            user.setImageUrl(imageUrl);
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("No role found"));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);
        return new MessageDto("Successfully registered!");

    }

    public ResponseEntity<?> login(LoginUserRequestDto loginUserRequestDto) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequestDto.getUsername(),
                        loginUserRequestDto.getPassword()
                )
        );
        context.setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println("USERDETAILS FROM LOGIN: " + userDetails.toString());

        ResponseCookie jwtCookie = jwtService.generateJwtCookie(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtService.generateJwtRefreshCookie(refreshToken.getToken());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        User user= userRepository.findById(userDetails.getId())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new LoginUserResponseDto(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        user.getImageUrl(),
                        roles));
    }

    public ResponseEntity<?> logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal from LOGOUT: " + principal);
        if (principal.toString() != "anonymousUser") {
            Long userId = ((UserDetailsImpl) principal).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtService.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageDto("You've been logged out"));
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtService.getJwtRefreshFromCookie(request);
        if (refreshToken != null && refreshToken.length() > 0) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtService.generateJwtCookie(user);
                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageDto("RT successfully refreshed"));
                    }).orElseThrow(() -> new TokenRefreshException(refreshToken, "RT is not in DB"));
        }
        return ResponseEntity.badRequest().body(new MessageDto("RT is empty"));
    }
}