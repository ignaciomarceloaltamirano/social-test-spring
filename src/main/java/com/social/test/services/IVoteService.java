package com.social.test.services;

import com.social.test.dtos.request.VoteRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.entities.Vote;

import java.util.List;

public interface IVoteService {
    MessageDto votePost(Long postId,Long userId, VoteRequestDto voteRequestDto);

    String getUserCurrentVote(Long postId, Long userId);

    List<Vote> getVotesByPostId(Long postId);
}
