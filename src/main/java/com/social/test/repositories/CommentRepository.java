package com.social.test.repositories;

import com.social.test.entities.Comment;
import com.social.test.entities.CommentVote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByAuthorId(Long userId, PageRequest p);
    List<Comment> findAllByPostId(Long postId);
//    List<Comment> findByReplyToId(Long replyToId);
@Query("SELECT c FROM Comment c WHERE c.replyTo.id = :replyToId")
List<Comment> findCommentsByReplyToId(Long replyToId);
}

