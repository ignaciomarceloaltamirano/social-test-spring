package com.social.test.services.impl;

import com.social.test.dtos.request.CommentVoteRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.entities.*;
import com.social.test.enumerations.EVoteType;
import com.social.test.repositories.CommentRepository;
import com.social.test.repositories.CommentVoteRepository;
import com.social.test.repositories.UserRepository;
import com.social.test.services.ICommentVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentVoteServiceImpl implements ICommentVoteService {
    private final CommentVoteRepository commentVoteRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public MessageDto voteComment(CommentVoteRequestDto commentVoteRequestDto, Long userId, Long commentId) {
        CommentVote existingVote = commentVoteRepository
                .findByUserIdAndCommentId(userId, commentId);
        EVoteType voteType = EVoteType.valueOf(commentVoteRequestDto.getType());
        if (existingVote != null) {
            if (voteType == existingVote.getType()) {
                commentVoteRepository.deleteByUserIdAndCommentId(userId, commentId);
                return new MessageDto("Vote deleted");
            } else {
                CommentVote updatingVote = commentVoteRepository.findByUserIdAndCommentId(userId, commentId);
                updatingVote.setType(voteType);
                return new MessageDto("Vote updated");
            }
        }
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        CommentVote newVote = CommentVote.builder()
                .user(user)
                .comment(comment)
                .type(voteType)
                .build();
        commentVoteRepository.save(newVote);
        return new MessageDto("Comment vote created");
    }

    public String getUserCurrentCommentVote(Long commentId, Long userId) {
        userRepository.findById(userId).orElseThrow();
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            EVoteType voteType = commentVoteRepository.findVoteTypeByUserAndComment(userId, commentId);
            return voteType != null ? voteType.name() : null;
        } else {
            return "Comment deleted";
        }
    }

    public List<CommentVote> getVotesByCommentId(Long commentId) {
        return commentVoteRepository.findAllByCommentId(commentId);
    }
}
