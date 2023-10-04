package com.social.test.services.impl;

import com.social.test.dtos.request.CommentRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.dtos.response.PageDto;
import com.social.test.entities.*;
import com.social.test.exceptions.ResourceNotFoundException;
import com.social.test.repositories.CommentRepository;
import com.social.test.repositories.CommentVoteRepository;
import com.social.test.repositories.PostRepository;
import com.social.test.repositories.UserRepository;
import com.social.test.services.ICommentService;
import com.social.test.services.IUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentVoteRepository commentVoteRepository;
    private final IUtilService utilService;

    public PageDto<Comment> getUserComments(Long userId, int page) {
        Sort s = Sort.by("id").ascending();
        int totalPages = commentRepository.findByAuthorId(userId, PageRequest.of(page - 1, 2, s)).getTotalPages();
        List<Comment> content = commentRepository.findByAuthorId(userId, PageRequest.of(page - 1, 2, s)).getContent();
        return new PageDto<>(content, totalPages);
    }

    public List<Comment> getPostComments(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public MessageDto deleteComment(Long commentId, Long userId) {
        commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment not found"));
        if (!Objects.equals(userId, utilService.getCurrentUser().getId())) {
            return new MessageDto("Unauthorized user");
        }
        commentRepository.deleteById(commentId);
        return new MessageDto("Comment deleted");
    }

    public Comment createComment(Long postId, Long userId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        if (commentRequestDto.getReplyToId() != null) {
            Comment replyTo = commentRepository.findById(commentRequestDto.getReplyToId()).orElseThrow();
            Comment comment = Comment.builder()
                    .author(user)
                    .post(post)
                    .text(commentRequestDto.getText())
                    .replyTo(replyTo)
                    .build();
            return commentRepository.save(comment);
        } else {
            Comment comment = Comment.builder()
                    .author(user)
                    .post(post)
                    .text(commentRequestDto.getText())
                    .build();
            return commentRepository.save(comment);
        }
    }
}
