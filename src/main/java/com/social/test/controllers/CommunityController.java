package com.social.test.controllers;

import com.social.test.dtos.request.CommunityRequestDto;
import com.social.test.dtos.response.CommunityResponseDto;
import com.social.test.entities.*;
import com.social.test.repositories.*;
import com.social.test.services.ICommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communities")
@RequiredArgsConstructor
public class CommunityController {
    private final ICommunityService communityService;


    @GetMapping
    public ResponseEntity<List<Community>> getCommunities() {
        return ResponseEntity.ok(communityService.getAllCommunities());
    }

    @GetMapping("/{communityName}")
    public ResponseEntity<Community> getCommunity(
            @PathVariable("communityName") String communityName) {
        return ResponseEntity.ok(communityService.getCommunity(communityName));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Community>> getUserSubscribedCommunities(
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(communityService.getUserSubscribedCommunities(userId));
    }

    @PostMapping
    public ResponseEntity<CommunityResponseDto> createCommunity(
            @RequestBody CommunityRequestDto communityRequestDto
    ) {
        return ResponseEntity.ok(communityService.createCommunity(communityRequestDto));
    }
}
