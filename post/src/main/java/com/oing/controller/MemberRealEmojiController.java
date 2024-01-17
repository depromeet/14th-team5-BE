package com.oing.controller;


import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.dto.response.RealEmojiResponse;
import com.oing.dto.response.RealEmojisResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.DuplicateRealEmojiException;
import com.oing.restapi.MemberRealEmojiApi;
import com.oing.service.MemberRealEmojiService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class MemberRealEmojiController implements MemberRealEmojiApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberRealEmojiService memberRealEmojiService;

    @Transactional
    @Override
    public PreSignedUrlResponse requestPresignedUrl(String memberId, PreSignedUrlRequest request) {
        validateMemberId(memberId);
        String imageName = request.imageName();
        return preSignedUrlGenerator.getRealEmojiPreSignedUrl(imageName);
    }

    @Transactional
    @Override
    public RealEmojiResponse createMemberRealEmoji(String memberId, CreateMyRealEmojiRequest request) {
        validateMemberId(memberId);
        String emojiId = identityGenerator.generateIdentity();
        String emojiImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());
        Emoji emoji = Emoji.fromString(request.type());
        if (isExistsSameRealEmojiType(emoji)) {
            throw new DuplicateRealEmojiException();
        }

        MemberRealEmoji realEmoji = new MemberRealEmoji(emojiId, memberId, emoji, request.imageUrl(), emojiImgKey);
        MemberRealEmoji addedRealEmoji = memberRealEmojiService.save(realEmoji);
        return RealEmojiResponse.from(addedRealEmoji);
    }

    private boolean isExistsSameRealEmojiType(Emoji emoji) {
        return memberRealEmojiService.findRealEmojiByEmojiType(emoji);
    }

    @Transactional
    @Override
    public RealEmojiResponse changeMemberRealEmoji(String memberId, String realEmojiId, UpdateMyRealEmojiRequest request) {
        validateMemberId(memberId);
        String emojiImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());

        MemberRealEmoji findEmoji = memberRealEmojiService.findRealEmojiById(realEmojiId);
        findEmoji.updateRealEmoji(request.imageUrl(), emojiImgKey);
        return RealEmojiResponse.from(findEmoji);
    }

    @Override
    public RealEmojisResponse getMemberRealEmojis(String memberId) {
        validateMemberId(memberId);

        List<MemberRealEmoji> realEmojis = memberRealEmojiService.findRealEmojisByMemberId(memberId);
        List<RealEmojiResponse> emojiResponses = realEmojis.stream()
                .map(RealEmojiResponse::from)
                .collect(Collectors.toList());
        return new RealEmojisResponse(emojiResponses);
    }

    private void validateMemberId(String memberId) {
        String loginId = authenticationHolder.getUserId();
        if (!loginId.equals(memberId)) {
            throw new AuthorizationFailedException();
        }
    }
}
