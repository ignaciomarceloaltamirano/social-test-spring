package com.social.test.services;

import com.social.test.dtos.request.PostRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.dtos.response.PageDto;
import com.social.test.entities.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPostService {
    PageDto<Post> getPostsByTag(String tagName, int page);

    PageDto<Post> getUserPosts(Long userId, int page);

    PageDto<Post> getUserUpVotedPosts(Long userId, int page);

    PageDto<Post> getUserDownVotedPosts(Long userId, int page);

    Post createPost(PostRequestDto postRequestDto, Long communityId, Long userId, MultipartFile file) throws IOException;

    MessageDto savePost(Long postId, Long userId);

    MessageDto unSavePost(Long postId, Long userId);

    MessageDto deletePost(Long postId, Long userId);

    PageDto<Post> getCommunityPosts(Long communityId, int page);

    Post getPost(Long postId);
    boolean isPostSavedByUser(Long postId,Long userId);

    PageDto<Post> getUserFollowedCommunitiesPosts(Long userId, int page);

    PageDto<Post> getUserSavedPosts(Long userId, int page);
}
