package com.social.test.controllers;

import com.social.test.dtos.response.PageDto;
import com.social.test.entities.Community;
import com.social.test.services.ISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final ISearchService searchService;

    @GetMapping("/{query}/page/{page}")
    public ResponseEntity<PageDto<Community>> getCommunities(
            @PathVariable("page") int page,
            @PathVariable("query") String query
    ) {
        return ResponseEntity.ok(searchService.getCommunities(page, query));
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<Community>> getCommunitiesByNameContaining(
            @PathVariable("query") String query
    ) {
        return ResponseEntity.ok(searchService.getCommunitiesByNameContaining(query));
    }
}
