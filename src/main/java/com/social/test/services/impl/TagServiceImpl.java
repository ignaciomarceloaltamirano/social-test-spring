package com.social.test.services.impl;

import com.social.test.entities.Post;
import com.social.test.entities.Tag;
import com.social.test.repositories.TagRepository;
import com.social.test.services.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {
    private final TagRepository tagRepository;

    public List<Tag> getTags() {
        List<Tag> allTags = tagRepository.findTagsWithPosts();

        for (Tag tag:allTags){
            System.out.println(tag.getPosts().stream().map(Post::getId));
        }
        return allTags;
    }
}
