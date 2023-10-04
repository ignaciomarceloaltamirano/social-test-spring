package com.social.test.services.impl;

import com.social.test.dtos.response.PageDto;
import com.social.test.entities.Community;
import com.social.test.repositories.CommunityRepository;
import com.social.test.services.ISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements ISearchService {
    private final CommunityRepository communityRepository;

    public PageDto<Community> getCommunities(int page, String query) {
        Sort s = Sort.by("id").ascending();
        int totalPages = communityRepository.findByNameContaining(query, PageRequest.of(page - 1, 2, s)).getTotalPages();
        List<Community> content = communityRepository.findByNameContaining(query, PageRequest.of(page - 1, 5, s)).getContent();
        return new PageDto<>(content, totalPages);
    }
    public List<Community> getCommunitiesByNameContaining( String query) {
        return communityRepository.findByNameContaining(query);
    }
}
