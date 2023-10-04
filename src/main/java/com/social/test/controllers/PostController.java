package com.social.test.controllers;

import com.social.test.dtos.response.MessageDto;
import com.social.test.dtos.response.PageDto;
import com.social.test.dtos.request.PostRequestDto;
import com.social.test.entities.*;
import com.social.test.services.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final IPostService postService;

    @GetMapping("/tags/{tagName}/page/{page}")
    public ResponseEntity<PageDto<Post>> getPostsByTag(
            @PathVariable("tagName") String tagName,
            @PathVariable("page") int page) {
        return ResponseEntity.ok(postService.getPostsByTag(tagName, page));
    }

    @GetMapping("/user/{userId}/page/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDto<Post>> getUserPosts(
            @PathVariable("userId") Long userId, @PathVariable("page") int page) {
        return ResponseEntity.ok(postService.getUserPosts(userId, page));
    }

    @GetMapping("/{userId}/saved/page/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDto<Post>> getUserSavedPosts(
            @PathVariable("userId") Long userId, @PathVariable("page") int page) {
        return ResponseEntity.ok(postService.getUserSavedPosts(userId, page));
    }

    @GetMapping("/user/{userId}/communities/page/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDto<Post>> getUserFollowedCommunitiesPosts(
            @PathVariable("userId") Long userId, @PathVariable("page") int page) {
        return ResponseEntity.ok(postService.getUserFollowedCommunitiesPosts(userId, page));
    }

    @GetMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Post> getPost(
            @PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/{postId}/is-saved/{userId}")
    public ResponseEntity<Boolean> isPostSavedByUser(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId) {;
        return ResponseEntity.ok(postService.isPostSavedByUser(postId, userId));
    }

    @GetMapping("/community/{communityId}/page/{page}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageDto<Post>> getCommunityPosts(
            @PathVariable("communityId") Long communityId, @PathVariable("page") int page) {
        return ResponseEntity.ok(postService.getCommunityPosts(communityId, page));
    }

    @GetMapping("/{userId}/upvoted/page/{page}")
    public ResponseEntity<PageDto<Post>> getUserUpVotedPosts(
            @PathVariable("userId") Long userId,
            @PathVariable("page") int page
    ) {
        return ResponseEntity.ok(postService.getUserUpVotedPosts(userId, page));
    }

    @GetMapping("/{userId}/downvoted/page/{page}")
    public ResponseEntity<PageDto<Post>> getUserDownVotedPosts(
            @PathVariable("userId") Long userId,
            @PathVariable("page") int page
    ) {
        return ResponseEntity.ok(postService.getUserDownVotedPosts(userId, page));
    }

    @PostMapping(value = "/{communityId}/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestPart(value = "post") PostRequestDto postRequestDto,
            @PathVariable("communityId") Long communityId,
            @PathVariable("userId") Long userId,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(postService.createPost(postRequestDto, communityId, userId, file));
    }

    @PostMapping("/save/{postId}/{userId}")
    public ResponseEntity<MessageDto> savePost(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(postService.savePost(postId, userId));
    }

    @DeleteMapping("/unsave/{postId}/{userId}")
    public ResponseEntity<MessageDto> unSavePost(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(postService.unSavePost(postId, userId));
    }

    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<MessageDto> deletePost(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(postService.deletePost(postId, userId));
    }
}
