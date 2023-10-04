package com.social.test.repositories;

import com.social.test.entities.Post;
import com.social.test.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthorId(Long userId, PageRequest p);

    Page<Post> findByCommunityId(Long communityId, Pageable p);

    Page<Post> findPostsByTagsName(String tagName, PageRequest p);

    Page<Post> findById(Long postId, PageRequest of);

    @Query("SELECT DISTINCT p FROM Post p " +
            "INNER JOIN p.community c " +
            "INNER JOIN Subscription s ON c.id = s.community.id " +
            "WHERE s.user.id = :userId")
    Page<Post> findPostsByUserSubscriptions(Long userId, Pageable p);

    @Query("SELECT p FROM Post p JOIN p.users u WHERE u.id = :userId")
    Page<Post> findUserSavedPosts(Long userId, Pageable p);
}
