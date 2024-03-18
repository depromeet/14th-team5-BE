package com.oing.service;

import com.oing.domain.Emoji;
import com.oing.domain.MemberRealEmoji;
import com.oing.dto.request.CreateMyRealEmojiRequest;
import com.oing.dto.request.UpdateMyRealEmojiRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.DuplicateRealEmojiException;
import com.oing.exception.RealEmojiNotFoundException;
import com.oing.repository.MemberRealEmojiRepository;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRealEmojiService {

    private final IdentityGenerator identityGenerator;
    private final MemberRealEmojiRepository memberRealEmojiRepository;
    private final PreSignedUrlGenerator preSignedUrlGenerator;


    public PreSignedUrlResponse requestPresignedUrl(String memberId, String loginMemberId, String imageName) {
        log.info("Member {} is trying to request member real emoji Pre-Signed URL", loginMemberId);
        validateMemberId(loginMemberId, memberId);

        PreSignedUrlResponse response = preSignedUrlGenerator.getRealEmojiPreSignedUrl(imageName);
        log.info("Member real emoji Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());
        return response;
    }

    public MemberRealEmoji save(String memberId, String loginMemberId, String loginFamilyId, CreateMyRealEmojiRequest request) {
        validateMemberId(loginMemberId, memberId);
        String emojiId = identityGenerator.generateIdentity();
        String emojiImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());
        Emoji emoji = Emoji.fromString(request.type());
        if (isExistsSameRealEmojiType(emoji, memberId, loginFamilyId)) {
            throw new DuplicateRealEmojiException();
        }

        MemberRealEmoji realEmoji = new MemberRealEmoji(emojiId, memberId, loginFamilyId, emoji, request.imageUrl(), emojiImgKey);
        return memberRealEmojiRepository.save(realEmoji);
    }

    public boolean isExistsSameRealEmojiType(Emoji emoji, String memberId, String familyId) {
        return memberRealEmojiRepository
                .findByTypeAndMemberIdAndFamilyId(emoji, memberId, familyId)
                .isPresent();
    }

    public MemberRealEmoji changeMemberRealEmoji(
            String memberId, String loginMemberId, String loginFamilyId, String realEmojiId, UpdateMyRealEmojiRequest request
    ) {
        validateMemberId(loginMemberId, memberId);
        String emojiImgKey = preSignedUrlGenerator.extractImageKey(request.imageUrl());

        MemberRealEmoji findEmoji = getMemberRealEmojiByIdAndFamilyId(realEmojiId, loginFamilyId);
        findEmoji.updateRealEmoji(request.imageUrl(), emojiImgKey);
        return findEmoji;
    }

    public MemberRealEmoji getMemberRealEmojiByIdAndFamilyId(String realEmojiId, String familyId) {
        return memberRealEmojiRepository
                .findByIdAndFamilyId(realEmojiId, familyId)
                .orElseThrow(RealEmojiNotFoundException::new);
    }

    public List<MemberRealEmoji> findRealEmojisByMemberIdAndFamilyId(String memberId, String loginMemberId, String familyId) {
        validateMemberId(loginMemberId, memberId);

        return memberRealEmojiRepository.findAllByMemberIdAndFamilyId(memberId, familyId);
    }

    private void validateMemberId(String loginMemberId, String memberId) {
        if (!loginMemberId.equals(memberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting to access member real emoji", loginMemberId);
            throw new AuthorizationFailedException();
        }
    }
}
