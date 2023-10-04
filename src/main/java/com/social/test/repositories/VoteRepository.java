package com.social.test.repositories;

import com.social.test.entities.Post;
import com.social.test.entities.User;
import com.social.test.entities.Vote;
import com.social.test.enumerations.EVoteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByPostId(Long postId);
    Vote findByUserIdAndPostId(Long userId, Long postId);
    @Query("SELECT v.type FROM Vote v WHERE v.user = :user AND v.post = :post")
    EVoteType findVoteTypeByUserAndPost(
            @Param("user") User user,
            @Param("post") Post post
    );
    void deleteByUserIdAndPostId(Long userId, Long postId);
    @Query("SELECT v.post FROM Vote v WHERE v.user = :user AND v.type = com.social.test.enumerations.EVoteType.UPVOTE")
    Page<Post> findUpvotedPostsByUser(@Param("user") User user, Pageable p);

    @Query("SELECT v.post FROM Vote v WHERE v.user = :user AND v.type = com.social.test.enumerations.EVoteType.DOWNVOTE")
    Page<Post> findDownVotedPostsByUser(@Param("user") User user, Pageable p);
}
