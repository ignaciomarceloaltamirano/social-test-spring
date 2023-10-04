package com.social.test.services.impl;

import com.social.test.dtos.request.CommunityRequestDto;
import com.social.test.dtos.response.CommunityResponseDto;
import com.social.test.entities.Community;
import com.social.test.entities.Subscription;
import com.social.test.entities.User;
import com.social.test.exceptions.ResourceNotFoundException;
import com.social.test.repositories.CommunityRepository;
import com.social.test.repositories.SubscriptionRepository;
import com.social.test.repositories.UserRepository;
import com.social.test.security.services.UserDetailsImpl;
import com.social.test.services.ICommunityService;
import com.social.test.services.IUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityServiceImpl implements ICommunityService {
    private final CommunityRepository communityRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final IUtilService utilService;

    public Community getCommunity(String communityName) {
        return communityRepository.findByName(communityName)
                .orElseThrow(()->new ResourceNotFoundException("Community not found"));
    }

    public CommunityResponseDto createCommunity(CommunityRequestDto communityRequestDto) {
        User user=utilService.getCurrentUser();
        Community community = Community.builder()
                .creator(user)
                .name(communityRequestDto.getName())
                .build();
        communityRepository.save(community);
        return new CommunityResponseDto(communityRequestDto.getName());
    }

    public List<Community> getUserSubscribedCommunities(Long userId) {
        List<Subscription> subscriptions= subscriptionRepository.getSubscriptionsByUserId(userId);
        List<Community> communities=new ArrayList<>();
        for (Subscription subscription:subscriptions){
            communities.add(subscription.getCommunity());
        }
        return communities;
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }
}
