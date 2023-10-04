package com.social.test.services;

import com.social.test.dtos.response.PageDto;
import com.social.test.entities.Community;

import java.util.List;

public interface ISearchService {
    PageDto<Community> getCommunities(int page, String query);

    List<Community> getCommunitiesByNameContaining(String query);
}
