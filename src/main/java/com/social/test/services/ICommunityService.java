package com.social.test.services;

import com.social.test.dtos.request.CommunityRequestDto;
import com.social.test.dtos.response.CommunityResponseDto;
import com.social.test.entities.Community;

import java.util.List;

public interface ICommunityService {
    Community getCommunity(String communityName);
    CommunityResponseDto createCommunity(CommunityRequestDto communityRequestDto);

    List<Community> getUserSubscribedCommunities(Long userId);

    List<Community> getAllCommunities();
}
