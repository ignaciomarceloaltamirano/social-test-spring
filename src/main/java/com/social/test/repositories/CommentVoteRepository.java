package com.social.test.repositories;

import com.social.test.entities.CommentVote;
import com.social.test.entities.Post;
import com.social.test.entities.User;
import com.social.test.entities.Vote;
import com.social.test.enumerations.EVoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentVoteRepository extends JpaRepository<CommentVote,Long> {
    CommentVote findByUserIdAndCommentId(Long userId, Long commentId);
    @Query("SELECT v.type FROM CommentVote v WHERE v.user.id = :userId AND v.comment.id = :commentId")
    EVoteType findVoteTypeByUserAndComment(@Param("userId") Long userId, @Param("commentId") Long commentId);
    List<CommentVote> findAllByCommentId(Long commentId);
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
}
