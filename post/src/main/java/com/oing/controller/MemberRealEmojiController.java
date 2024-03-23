package com.oing.controller;


import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.dto.response.RealEmojiResponse;
import com.oing.dto.response.RealEmojisResponse;
import com.oing.restapi.MemberRealEmojiApi;
import com.oing.service.MemberRealEmojiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberRealEmojiController implements MemberRealEmojiApi {

    private final MemberRealEmojiService memberRealEmojiService;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(String memberId, String loginMemberId, PreSignedUrlRequest request) {
        return memberRealEmojiService.requestPresignedUrl(memberId, loginMemberId, request.imageName());
    }

    @Override
    public RealEmojiResponse createMemberRealEmoji(String memberId, String loginMemberId, String loginFamilyId,
                                                   CreateMyRealEmojiRequest request) {
        log.info("Member {} is trying to create member real emoji", loginMemberId);
        MemberRealEmoji addedRealEmoji = memberRealEmojiService.save(memberId, loginMemberId, loginFamilyId, request);

        log.info("Member {} has created member real emoji {}", loginMemberId, addedRealEmoji.getId());
        return RealEmojiResponse.from(addedRealEmoji);
    }

    @Override
    public RealEmojiResponse changeMemberRealEmoji(
            String memberId, String loginMemberId, String loginFamilyId, String realEmojiId, UpdateMyRealEmojiRequest request
    ) {
        log.info("Member {} is trying to change member real emoji", loginMemberId);
        MemberRealEmoji changedEmoji = memberRealEmojiService.changeMemberRealEmoji(memberId, loginMemberId, loginFamilyId, realEmojiId, request);
        log.info("Member {} has changed member real emoji {}", loginMemberId, changedEmoji.getId());

        return RealEmojiResponse.from(changedEmoji);
    }

    @Override
    public RealEmojisResponse getMemberRealEmojis(String memberId, String loginMemberId, String loginFamilyId) {
        List<MemberRealEmoji> realEmojis = memberRealEmojiService.findRealEmojisByMemberIdAndFamilyId(memberId,
                loginMemberId, loginFamilyId);
        List<RealEmojiResponse> emojiResponses = realEmojis.stream()
                .map(RealEmojiResponse::from)
                .collect(Collectors.toList());
        return new RealEmojisResponse(emojiResponses);
    }
}
