package com.social.test.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResponseDto {
   private Long id;
   private String username;
   private String imageUrl;
   private List<String> roles;
}
