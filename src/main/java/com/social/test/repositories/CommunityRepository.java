package com.social.test.repositories;

import com.social.test.entities.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.ContentHandler;
import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community,Long> {
    Page<Community> findByNameContaining(String query, Pageable p);
    Optional<Community> findByName(String name);

    List<Community> findByNameContaining(String query);
}
