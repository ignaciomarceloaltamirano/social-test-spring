package com.social.test.services;

public interface ISubscriptionService {
    Boolean getSubscription(Long communityId);
    Object createSubscription(Long communityId);
    Object deleteSubscription(Long communityId);

    int getSubscriptionsByCommunity(Long communityId);
}
