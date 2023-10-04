package com.social.test.repositories;

import com.social.test.entities.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    @Query("SELECT DISTINCT t FROM Tag t JOIN FETCH t.posts p WHERE SIZE(t.posts) > 0")
    List<Tag> findTagsWithPosts();
    Page<Tag> findTagsByPostsId(Long postId, PageRequest p);

    Optional<Tag> findByName(String name);
}
