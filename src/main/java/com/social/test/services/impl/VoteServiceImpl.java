package com.social.test.services.impl;

import com.social.test.dtos.request.VoteRequestDto;
import com.social.test.dtos.response.MessageDto;
import com.social.test.entities.Post;
import com.social.test.entities.User;
import com.social.test.entities.Vote;
import com.social.test.enumerations.EVoteType;
import com.social.test.repositories.PostRepository;
import com.social.test.repositories.UserRepository;
import com.social.test.repositories.VoteRepository;
import com.social.test.services.IVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements IVoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public MessageDto votePost(Long postId, Long userId, VoteRequestDto voteRequestDto) {
        Vote existingVote = voteRepository
                .findByUserIdAndPostId(userId, postId);
        EVoteType voteType = EVoteType.valueOf(voteRequestDto.getType());

        if (existingVote != null) {
            if (existingVote.getType() == voteType) {
                voteRepository.deleteByUserIdAndPostId(userId, postId);
                return new MessageDto("Vote deleted");
            } else {
                Vote updatingVote = voteRepository
                        .findByUserIdAndPostId(userId, postId);
                updatingVote.setType(voteType);
                return new MessageDto("Vote updated");
            }
        } else {
            User user = userRepository.findById(userId).orElseThrow();
            Post post = postRepository.findById(postId).orElseThrow();
            Vote newVote = Vote.builder()
                    .user(user)
                    .post(post)
                    .type(voteType)
                    .build();
            voteRepository.save(newVote);
            return new MessageDto("Vote created");
        }
    }

    public String getUserCurrentVote(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            EVoteType voteType = voteRepository.findVoteTypeByUserAndPost(user, post);
            return voteType != null ? voteType.name() : null;
        } else {
            return "Post deleted";
        }
    }

    public List<Vote> getVotesByPostId(Long postId) {
        return voteRepository.findAllByPostId(postId);
    }
}
