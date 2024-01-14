package com.oing.controller;


import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.dto.response.RealEmojisResponse;
import com.oing.restapi.MemberRealEmojiApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MemberRealEmojiController implements MemberRealEmojiApi {

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        return new PreSignedUrlResponse("https://test/2021-08-22/real-emoji-1.jpg");
    }

    @Override
    public DefaultResponse createMyRealEmoji(CreateMyRealEmojiRequest request) {
        return new DefaultResponse(true);
    }

    @Override
    public DefaultResponse changeMyRealEmoji(String realEmojiId, CreateMyRealEmojiRequest request) {
        return new DefaultResponse(true);
    }

    @Override
    public RealEmojisResponse getMyRealEmojis(String memberId) {
        return new RealEmojisResponse(null);
    }
}
