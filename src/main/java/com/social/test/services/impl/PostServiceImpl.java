package com.social.test.services.impl;

import com.social.test.dtos.request.PostRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.dtos.response.PageDto;
import com.social.test.entities.*;
import com.social.test.repositories.*;
import com.social.test.services.IFileUploadService;
import com.social.test.services.IPostService;
import com.social.test.services.IUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final CommunityRepository communityRepository;
    private final TagRepository tagRepository;
    private final IFileUploadService fileUploadService;
    private final IUtilService utilService;

    public PageDto<Post> getPostsByTag(String tagName, int page) {
        Sort s = Sort.by("id").ascending();
        int totalPages = postRepository.findPostsByTagsName(tagName, PageRequest.of(page - 1, 6, s)).getTotalPages();
        List<Post> content = postRepository.findPostsByTagsName(tagName, PageRequest.of(page - 1, 6, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public PageDto<Post> getCommunityPosts(Long communityId, int page) {
        Sort s = Sort.by("createdAt").descending();
        int totalPages = postRepository.findByCommunityId(communityId, PageRequest.of(page - 1, 6, s)).getTotalPages();
        List<Post> content = postRepository.findByCommunityId(communityId, PageRequest.of(page - 1, 6, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public PageDto<Post> getUserSavedPosts(Long userId, int page) {
        Sort s = Sort.by("id").descending();
        int totalPages = postRepository.findUserSavedPosts(userId,PageRequest.of(page-1,6,s)).getTotalPages();
        List<Post> content = postRepository.findUserSavedPosts(userId, PageRequest.of(page - 1, 6, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public PageDto<Post> getUserFollowedCommunitiesPosts(Long userId, int page) {
        Sort s = Sort.by("id").descending();
        int totalPages = postRepository.findPostsByUserSubscriptions(userId,PageRequest.of(page-1,6,s)).getTotalPages();
        List<Post> content = postRepository.findPostsByUserSubscriptions(userId, PageRequest.of(page - 1, 6, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow();
    }

    public boolean isPostSavedByUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return post.getUsers().stream().anyMatch(user -> user.getId().equals(userId));
        }
        return false;
    }

    public PageDto<Post> getUserPosts(Long userId, int page) {
        Sort s = Sort.by("id").ascending();
        int totalPages = postRepository.findByAuthorId(userId, PageRequest.of(page - 1, 6, s)).getTotalPages();
        List<Post> content = postRepository.findByAuthorId(userId, PageRequest.of(page - 1, 6, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public PageDto<Post> getUserUpVotedPosts(Long userId, int page) {
        User user = userRepository.findById(userId).orElseThrow();
        Sort s = Sort.by("id").ascending();
        int totalPages = voteRepository.findUpvotedPostsByUser(user, PageRequest.of(page - 1, 6, s)).getTotalPages();
        List<Post> content = voteRepository.findUpvotedPostsByUser(user, PageRequest.of(page - 1, 6, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public PageDto<Post> getUserDownVotedPosts(Long userId, int page) {
        User user = userRepository.findById(userId).orElseThrow();
        Sort s = Sort.by("id").ascending();
        int totalPages = voteRepository.findDownVotedPostsByUser(user, PageRequest.of(page - 1, 6, s)).getTotalPages();
        List<Post> content = voteRepository.findDownVotedPostsByUser(user, PageRequest.of(page - 1, 6, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public Post createPost(PostRequestDto postRequestDto, Long communityId, Long userId, MultipartFile file) throws
            IOException {
        User user = userRepository.findById(userId).orElseThrow();
        Community community = communityRepository.findById(communityId).orElseThrow();
        Set<Tag> tags = new HashSet<>();
        List<Tag> newTags = new ArrayList<>(); // Collect new tags to save in a batch

        Post post = Post.builder()
                .content(postRequestDto.getContent())
                .title(postRequestDto.getTitle())
                .author(user)
                .community(community)
                .build();
        for (String tagName : postRequestDto.getTags()) {
            Optional<Tag> existingTag = tagRepository.findByName(tagName);
            if (existingTag.isPresent()) {
                tags.add(existingTag.get());
            } else {
                Tag newTag = Tag.builder()
                        .name(tagName)
                        .build();
                newTags.add(newTag);
            }
        }
        // Save new tags in a batch
        if (!newTags.isEmpty()) {
            newTags = tagRepository.saveAll(newTags);
            tags.addAll(newTags);
        }
        post.setTags(tags);
        if (file != null) {
            String imageUrl = fileUploadService.uploadFile(file);
            post.setImageUrl(imageUrl);
        }
        return postRepository.save(post);
    }

    public MessageDto savePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        user.savePost(post);
        userRepository.save(user);
        return new MessageDto("Post saved");
    }

    public MessageDto unSavePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        user.unSavePost(post);
        userRepository.save(user);
        return new MessageDto("Post unsaved");
    }

    public MessageDto deletePost(Long postId, Long userId) {
        postRepository.findById(postId).orElseThrow();
        if (Objects.equals(userId, utilService.getCurrentUser().getId())) {
            postRepository.deleteById(postId);
            return new MessageDto("Post deleted");
        }
        return new MessageDto("Unauthorized user");
    }
}
