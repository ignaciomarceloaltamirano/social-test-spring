package com.social.test.controllers;

import com.social.test.entities.Community;
import com.social.test.services.ISubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final ISubscriptionService subscriptionService;

    @GetMapping("/{communityId}")
    public ResponseEntity<Boolean> getSubscription(
            @PathVariable("communityId") Long communityId) {
        return ResponseEntity.ok(subscriptionService.getSubscription(communityId));
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<Integer> getSubscriptionsByCommunity(
            @PathVariable("communityId") Long communityId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByCommunity(communityId));
    }

    @PostMapping("/subscribe/{communityId}")
    public ResponseEntity<Object> createSubscription(
            @PathVariable("communityId") Long communityId) {
        return ResponseEntity.ok(subscriptionService.createSubscription(communityId));
    }

    @Transactional
    @DeleteMapping("/unsubscribe/{communityId}")
    public ResponseEntity<Object> deleteSubscription(
            @PathVariable("communityId") Long communityId
    ) {
        return ResponseEntity.ok(subscriptionService.deleteSubscription(communityId));
    }
}
