package com.oing.controller;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.dto.request.PostReactionRequest;
import com.oing.restapi.PostReactionApi;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostReactionController implements PostReactionApi {
    @Override
    public void createPostReaction(String postId, PostReactionRequest request) {
        String writerIdBase = "01HGW2N7EHJVJ4CJ888RRS2E";

        String content = request.content();
        validateEmojiContent(content);
        // TODO: 해당 게시물 찾기 및 해당 게시물에 같은 이모지 남긴 적 있는지 확인
        // TODO: 해당 게시물에 이모지 남기고 이모지 개수 증가
    }

    @Override
    public void deletePostReaction(String postId, PostReactionRequest request) {
        String writerIdBase = "01HGW2N7EHJVJ4CJ888RRS2E";

        String content = request.content();
        validateEmojiContent(content);
        // TODO: 해당 게시물 찾기 및 해당 게시물에 같은 이모지 남긴 적 있는지 확인
        // TODO: 해당 게시물에 이모지 삭제하고 이모지 개수 감소
    }

    private void validateEmojiContent(String content) {
        List<String> allowedEmojis = List.of("smile", "heart", "clap", "thumbs-up", "laugh");

        if (!allowedEmojis.contains(content)) {
            throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
