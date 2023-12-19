package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.domain.model.MemberPost;
import com.oing.dto.request.PostReactionRequest;
import com.oing.exception.EmojiAlreadyExistsException;
import com.oing.restapi.PostReactionApi;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PostReactionController implements PostReactionApi {

    private final AuthenticationHolder authenticationHolder;
    private final MemberPostService memberPostService;

    @Override
    public void createPostReaction(String postId, PostReactionRequest request) {
        String memberId = authenticationHolder.getUserId();
        Emoji emoji = Emoji.fromString(request.content());
        MemberPost post = memberPostService.findMemberPostById(postId);

        if (memberPostService.isMemberPostReactionExists(post, memberId, emoji)) {
            throw new EmojiAlreadyExistsException();
        }
        addPostReaction(post, memberId, emoji);

        // TODO: 해당 게시물에 이모지 남기고 이모지 개수 증가
    }

    private void addPostReaction(MemberPost post, String memberId, Emoji emoji) {

    }

    @Override
    public void deletePostReaction(String postId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());

        // TODO: 해당 게시물 찾기 및 해당 게시물에 같은 이모지 남긴 적 있는지 확인
        // TODO: 해당 게시물에 이모지 삭제하고 이모지 개수 감소
    }
}
