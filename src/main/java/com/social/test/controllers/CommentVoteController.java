package com.social.test.controllers;

import com.social.test.dtos.request.CommentVoteRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.entities.CommentVote;
import com.social.test.entities.Vote;
import com.social.test.services.ICommentVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commentvotes")
@RequiredArgsConstructor
public class CommentVoteController {
    private final ICommentVoteService commentVoteService;

    @Transactional
    @PutMapping("/{commentId}/{userId}")
    public ResponseEntity<MessageDto> voteComment(
            @RequestBody CommentVoteRequestDto commentVoteRequestDto,
            @PathVariable("commentId") Long commentId,
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(commentVoteService.voteComment(commentVoteRequestDto, userId, commentId));
    }

    @GetMapping("/{commentId}/{userId}")
    public ResponseEntity<String> getUserCurrentCommentVote(
            @PathVariable("commentId") Long commentId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(commentVoteService.getUserCurrentCommentVote(commentId, userId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<List<CommentVote>> getCommentVotesByCommentId(
            @PathVariable("commentId") Long commentId
    ) {
        return ResponseEntity.ok(commentVoteService.getVotesByCommentId(commentId));
    }
}
