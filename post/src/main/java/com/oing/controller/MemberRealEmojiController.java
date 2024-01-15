package com.oing.controller;


import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.dto.response.RealEmojisResponse;
import com.oing.restapi.MemberRealEmojiApi;
import com.oing.util.PreSignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MemberRealEmojiController implements MemberRealEmojiApi {

    private final PreSignedUrlGenerator preSignedUrlGenerator;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(String memberId, PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
    }

    @Override
    public DefaultResponse createMyRealEmoji(String memberId, CreateMyRealEmojiRequest request) {
        return new DefaultResponse(true);
    }

    @Override
    public DefaultResponse changeMyRealEmoji(String memberId, String realEmojiId, CreateMyRealEmojiRequest request) {
        return new DefaultResponse(true);
    }

    @Override
    public RealEmojisResponse getMyRealEmojis(String memberId) {
        return new RealEmojisResponse(null);
    }
}
