package com.social.test.controllers;

import com.social.test.dtos.request.VoteRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.entities.Vote;
import com.social.test.services.IVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private final IVoteService voteService;

    @Transactional
    @PutMapping("/{postId}/{userId}")
    public ResponseEntity<MessageDto> votePost(
            @RequestBody VoteRequestDto voteRequestDto,
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(voteService.votePost(postId, userId, voteRequestDto));
    }

    @GetMapping("/{postId}/{userId}")
    public ResponseEntity<String> getUserCurrentVote(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(voteService.getUserCurrentVote(postId, userId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Vote>> getVotesByPostId(
            @PathVariable("postId") Long postId
    ) {
        return ResponseEntity.ok(voteService.getVotesByPostId(postId));
    }
}
