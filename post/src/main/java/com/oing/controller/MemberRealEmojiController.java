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
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberRealEmojiController implements MemberRealEmojiApi {

    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberRealEmojiService memberRealEmojiService;

    @Transactional
    @Override
    public PreSignedUrlResponse requestPresignedUrl(String memberId, String loginMemberId, PreSignedUrlRequest request) {
        log.info("Member {} is trying to request member real emoji Pre-Signed URL", loginMemberId);
        validateMemberId(loginMemberId, memberId);
        String imageName = request.imageName();

        PreSignedUrlResponse response = preSignedUrlGenerator.getRealEmojiPreSignedUrl(imageName);
        log.info("Member real emoji Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());

        return response;
    }

    @Transactional
    @Override
    public RealEmojiResponse createMemberRealEmoji(String memberId, String loginMemberId, String loginFamilyId,
                                                   CreateMyRealEmojiRequest request) {
        log.info("Member {} is trying to create member real emoji", loginMemberId);
        validateMemberId(loginMemberId, memberId);

        String emojiId = identityGenerator.generateIdentity();
        String emojiImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());
        Emoji emoji = Emoji.fromString(request.type());
        if (isExistsSameRealEmojiType(emoji, memberId, loginFamilyId)) {
            throw new DuplicateRealEmojiException();
        }

        MemberRealEmoji realEmoji = new MemberRealEmoji(emojiId, memberId, loginFamilyId, emoji, request.imageUrl(), emojiImgKey);
        MemberRealEmoji addedRealEmoji = memberRealEmojiService.save(realEmoji);
        log.info("Member {} has created member real emoji {}", loginMemberId, emojiId);

        return RealEmojiResponse.from(addedRealEmoji);
    }

    private boolean isExistsSameRealEmojiType(Emoji emoji, String memberId, String familyId) {
        return memberRealEmojiService.findRealEmojiByEmojiTypeAndMemberIdAndFamilyId(emoji, memberId, familyId);
    }

    @Transactional
    @Override
    public RealEmojiResponse changeMemberRealEmoji(
            String memberId, String loginMemberId, String loginFamilyId, String realEmojiId, UpdateMyRealEmojiRequest request
    ) {
        log.info("Member {} is trying to change member real emoji", loginMemberId);
        validateMemberId(loginMemberId, memberId);
        String emojiImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());

        MemberRealEmoji findEmoji = memberRealEmojiService.getMemberRealEmojiByIdAndFamilyId(realEmojiId, loginFamilyId);
        findEmoji.updateRealEmoji(request.imageUrl(), emojiImgKey);
        log.info("Member {} has changed member real emoji {}", loginMemberId, findEmoji.getId());

        return RealEmojiResponse.from(findEmoji);
    }

    @Override
    public RealEmojisResponse getMemberRealEmojis(String memberId, String loginMemberId, String loginFamilyId) {
        validateMemberId(loginMemberId, memberId);

        List<MemberRealEmoji> realEmojis = memberRealEmojiService.findRealEmojisByMemberIdAndFamilyId(memberId, loginFamilyId);
        List<RealEmojiResponse> emojiResponses = realEmojis.stream()
                .map(RealEmojiResponse::from)
                .collect(Collectors.toList());
        return new RealEmojisResponse(emojiResponses);
    }

    private void validateMemberId(String loginMemberId, String memberId) {
        if (!loginMemberId.equals(memberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access member real emoji", loginMemberId);
            throw new AuthorizationFailedException();
        }
    }
}
