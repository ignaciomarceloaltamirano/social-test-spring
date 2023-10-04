package com.social.test.services.impl;

import com.social.test.dtos.response.MessageDto;
import com.social.test.dtos.response.SubscriptionResponseDto;
import com.social.test.entities.Community;
import com.social.test.entities.Subscription;
import com.social.test.entities.User;
import com.social.test.exceptions.ResourceNotFoundException;
import com.social.test.repositories.CommunityRepository;
import com.social.test.repositories.SubscriptionRepository;
import com.social.test.services.ISubscriptionService;
import com.social.test.services.IUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements ISubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CommunityRepository communityRepository;
    private final IUtilService utilService;

    public Boolean getSubscription(Long communityId) {
        User user = utilService.getCurrentUser();
        return subscriptionRepository.existsByCommunityIdAndUserId(communityId, user.getId());
    }

    public int getSubscriptionsByCommunity(Long communityId) {
        return subscriptionRepository.getSubscriptionsByCommunityId(communityId).size();
    }

    public Object createSubscription(Long communityId) {
        User user = utilService.getCurrentUser();
        boolean subscriptionExists = subscriptionRepository
                .existsByCommunityIdAndUserId(communityId, user.getId());

        if (subscriptionExists) {
            return new MessageDto("Already subscribed");
        } else {
            Community community = communityRepository.findById(communityId)
                    .orElseThrow(() -> new ResourceNotFoundException("Community not found"));
            Subscription subscription = Subscription.builder()
                    .user(user)
                    .community(community)
                    .build();
            subscriptionRepository.save(subscription);
            return new SubscriptionResponseDto(subscription.getCommunity().getName());
        }
    }

    public Object deleteSubscription(Long communityId) {
        User user = utilService.getCurrentUser();
        boolean subscriptionExists = subscriptionRepository
                .existsByCommunityIdAndUserId(communityId, user.getId());

        if (!subscriptionExists) {
            return new MessageDto("You are not subscribed to this community yet");
        } else {
            subscriptionRepository
                    .deleteByCommunityIdAndUserId(communityId, user.getId());
            Community community=communityRepository.findById(communityId)
                    .orElseThrow(()->new ResourceNotFoundException("Community not found"));
            return new SubscriptionResponseDto(community.getName());
        }
    }
}
