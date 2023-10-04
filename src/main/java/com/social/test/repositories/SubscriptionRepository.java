package com.social.test.repositories;

import com.social.test.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Boolean existsByCommunityIdAndUserId(Long communityId, Long userId);

    List<Subscription> getSubscriptionsByCommunityId(Long community);
    List<Subscription> getSubscriptionsByUserId(Long userId);

    void deleteByCommunityIdAndUserId(Long communityId, Long userId);
}
