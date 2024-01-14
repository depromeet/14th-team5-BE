package com.oing.controller;


import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.*;
import com.oing.restapi.MemberPostRealEmojiApi;
import com.oing.restapi.MemberRealEmojiApi;
import com.oing.service.MemberBridge;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Collections;

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
    public MyPostRealEmojisResponse getMyRealEmojis(String memberId) {
        return new MyPostRealEmojisResponse(Collections.emptyMap());
    }
}
