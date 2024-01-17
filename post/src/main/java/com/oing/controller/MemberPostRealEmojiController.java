package com.oing.controller;


import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.response.ArrayResponse;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PostRealEmojiResponse;
import com.oing.restapi.MemberPostRealEmojiApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberPostRealEmojiController implements MemberPostRealEmojiApi {

    @Override
    public DefaultResponse createRealEmoji(String postId, PostRealEmojiRequest request) {
        return new DefaultResponse(true);
    }

    @Override
    public DefaultResponse deleteRealEmoji(String postId, String realEmojiId) {
        return new DefaultResponse(true);
    }

    @Override
    public ArrayResponse<PostRealEmojiResponse> getPostRealEmojis(String postId) {
        List<PostRealEmojiResponse> mockResponses = Arrays.asList(
                new PostRealEmojiResponse("01HGW2N7EHJVJ4CJ999RRS2E97", "emoji_1", postId,
                        "01HUUDFAOHJVJ4CJ999RRS2E97", "http://test.com/images/test1.jpg"),
                new PostRealEmojiResponse("01HGW2N7EHJVJ4CJ999RRS2E97", "emoji_2", postId,
                        "01HGW2N7EHJVJ4CJ999RRS2E97", "http://test.com/images/test2.jpg"),
                new PostRealEmojiResponse("01DGW2N7EFFFEDFAG9RRS2E976", "emoji_2", postId,
                        "01HGW2N7EHJVJEEFD99RRS2E97", "http://test.com/images/test2.jpg")
        );

        return ArrayResponse.of(mockResponses);
    }
}
