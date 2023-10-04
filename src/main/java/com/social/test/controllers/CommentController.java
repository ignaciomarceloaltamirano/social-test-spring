package com.social.test.controllers;

import com.social.test.dtos.request.CommentRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.dtos.response.PageDto;
import com.social.test.entities.Comment;
import com.social.test.services.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService commentService;

    @GetMapping("/{userId}/page/{page}")
    public ResponseEntity<PageDto<Comment>> getUserComments(
            @PathVariable("userId") Long userId,
            @PathVariable("page") int page
    ) {
        return ResponseEntity.ok(commentService.getUserComments(userId,page));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getPostComments(
            @PathVariable("postId") Long postId
    ) {
        return ResponseEntity.ok(commentService.getPostComments(postId));
    }

    @PutMapping("/{postId}/{userId}")
    public ResponseEntity<Comment> createComment(
            @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable("postId")Long postId,
            @PathVariable("userId")Long userId
    ) {
        return ResponseEntity.ok(commentService.createComment(postId,userId,commentRequestDto));
    }

    @DeleteMapping("/{commentId}/{userId}")
    public ResponseEntity<MessageDto> deleteComment(
            @PathVariable("commentId") Long commentId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(commentService.deleteComment(commentId, userId));
    }
}
