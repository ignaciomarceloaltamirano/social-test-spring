package com.social.test.services;

import com.social.test.dtos.request.CommentRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.dtos.response.PageDto;
import com.social.test.entities.Comment;

import java.util.List;

public interface ICommentService {
    PageDto<Comment> getUserComments(Long userId, int page);

    Comment createComment(Long postId,Long userId,CommentRequestDto commentRequestDto);

    List<Comment> getPostComments(Long postId);

    MessageDto deleteComment(Long commentId, Long userId);
}
