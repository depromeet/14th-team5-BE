package com.oing.controller;

import com.oing.domain.Emoji;
import com.oing.dto.request.PostReactionRequest;
import com.oing.restapi.PostReactionApi;
import org.springframework.stereotype.Controller;

@Controller
public class PostReactionController implements PostReactionApi {
    @Override
    public void createPostReaction(String postId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());

        // TODO: 해당 게시물 찾기 및 해당 게시물에 같은 이모지 남긴 적 있는지 확인
        // TODO: 해당 게시물에 이모지 남기고 이모지 개수 증가
    }

    @Override
    public void deletePostReaction(String postId, PostReactionRequest request) {
        Emoji emoji = Emoji.fromString(request.content());

        // TODO: 해당 게시물 찾기 및 해당 게시물에 같은 이모지 남긴 적 있는지 확인
        // TODO: 해당 게시물에 이모지 삭제하고 이모지 개수 감소
    }
}
