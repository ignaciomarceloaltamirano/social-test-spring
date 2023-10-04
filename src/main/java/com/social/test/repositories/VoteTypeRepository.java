package com.social.test.repositories;

import com.social.test.entities.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteTypeRepository extends JpaRepository<VoteType,Long> {
}
