package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.model.MemberPost;
import com.oing.domain.model.MemberPostReaction;
import com.oing.dto.request.PostReactionRequest;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.restapi.PostReactionApi;
import com.oing.service.MemberPostService;
import com.oing.service.PostReactionService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PostReactionController implements PostReactionApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final MemberPostService memberPostService;
    private final PostReactionService postReactionService;

    @Override
    @Transactional
    public void createPostReaction(String postId, PostReactionRequest request) {
        String memberId = authenticationHolder.getUserId();
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        if (postReactionService.isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiAlreadyExistsException();
        }
        addPostReaction(post, memberId, emoji);
    }

    private void addPostReaction(MemberPost post, String memberId, Emoji emoji) {
        String reactionId = identityGenerator.generateIdentity();
        MemberPostReaction reaction = postReactionService.createPostReaction(reactionId, post, memberId, emoji);
        post.addReaction(reaction);
    }

    @Override
    public void deletePostReaction(String postId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());

        // TODO: 해당 게시물 찾기 및 해당 게시물에 같은 이모지 남긴 적 있는지 확인
        // TODO: 해당 게시물에 이모지 삭제하고 이모지 개수 감소
    }
}
