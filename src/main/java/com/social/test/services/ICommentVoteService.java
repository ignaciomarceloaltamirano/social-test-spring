package com.social.test.services;

import com.social.test.dtos.request.CommentVoteRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.entities.CommentVote;

import java.util.List;

public interface ICommentVoteService {
    MessageDto voteComment(CommentVoteRequestDto commentVoteRequestDto, Long userId, Long commentId);

    String getUserCurrentCommentVote(Long commentId, Long userId);

    List<CommentVote> getVotesByCommentId(Long commentId);
}
