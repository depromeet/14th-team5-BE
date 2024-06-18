package com.oing.controller;

import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.oing.domain.Member;
import com.oing.domain.MemberPick;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.QuitMemberRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.dto.response.*;
import com.oing.exception.AuthorizationFailedException;
import com.oing.exception.PickFailedAlreadyUploadedException;
import com.oing.restapi.MemberApi;
import com.oing.service.*;
import com.oing.util.FCMNotificationUtil;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.oing.exception.ErrorCode.UNAUTHORIZED_MEMBER_USED;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberPickService memberPickService;
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final PostBridge postBridge;
    private final FCMNotificationService fcmNotificationService;


    @Override
    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMembersProfiles(
            Integer page, Integer size, String loginFamilyId
    ) {
        Page<FamilyMemberProfileResponse> profilePage = memberService.getFamilyMembersProfilesByFamilyId(
                loginFamilyId, page, size
        );

        PaginationDTO<FamilyMemberProfileResponse> paginationDTO = PaginationDTO.of(profilePage);
        return PaginationResponse.of(paginationDTO, page, size);
    }

    @Override
    public MemberResponse getMember(String memberId, String loginFamilyId) {
        validateFamilyMember(memberId, loginFamilyId);

        Member member = memberService.getMemberByMemberId(memberId);
        return MemberResponse.of(member);
    }

    // 가족 링크 초대 정보 조회 API에서 사용되는 메서드
    @Override
    public MemberResponse getMemberNullable(String memberId) {
        Member member = memberService.getMemberByMemberIdOrNull(memberId);
        if (member == null) {
            return new MemberResponse(null, null, null, null, null, null);
        }
        return MemberResponse.of(member);
    }

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getProfileImagePreSignedUrl(imageName);
    }

    @Override
    public MemberResponse updateMemberProfileImageUrl(String memberId, UpdateMemberProfileImageUrlRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.updateMemberProfileImageUrl(memberId, request.profileImageUrl());
        return MemberResponse.of(member);
    }

    @Override
    public MemberResponse deleteMemberProfileImageUrl(String memberId, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.deleteMemberProfileImageUrl(memberId);
        return MemberResponse.of(member);
    }

    @Override
    public MemberResponse updateMemberName(String memberId, UpdateMemberNameRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        Member member = memberService.updateMemberName(memberId, request.name());
        return MemberResponse.of(member);
    }

    @Override
    public DefaultResponse deleteMember(String memberId, QuitMemberRequest request, String loginMemberId) {
        validateMemberIdMatch(memberId, loginMemberId);

        memberService.deleteMember(memberId, request);
        return DefaultResponse.ok();
    }


    private void validateFamilyMember(String memberId, String loginFamilyId) {
        if (!memberService.isFamilyMember(memberId, loginFamilyId)) {
            throw new AuthorizationFailedException(UNAUTHORIZED_MEMBER_USED);
        }
    }

    @Transactional
    @Override
    public DefaultResponse pickMember(String memberId, String loginMemberId, String loginFamilyId) {
        if (postBridge.isUploadedToday(loginFamilyId, memberId)) {
            throw new PickFailedAlreadyUploadedException();
        }
        Member toMember = memberService.getMemberByMemberId(memberId);
        MemberPick memberPick = memberPickService.pickMember(loginFamilyId, loginMemberId, memberId);

        Member fromMember = memberService.getMemberByMemberId(memberPick.getFromMemberId());

        List<String> tokens = memberDeviceService.getFcmTokensByMemberId(toMember.getId());
        if (!tokens.isEmpty()) {
            Notification notification = FCMNotificationUtil
                    .buildNotification(String.format("%s님, 바쁘신가요?", toMember.getName()),
                        String.format("%s님이 당신의 생존을 궁금해해요.", fromMember.getName()));
            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(notification)
                    .addAllTokens(tokens)
                    .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                    .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                    .build();
            fcmNotificationService.sendMulticastMessage(message);
        }

        return DefaultResponse.ok();
    }

    @Override
    public ArrayResponse<MemberResponse> getPickMembers(String memberId, String loginFamilyId) {
        //나를 찌른 사람들
        List<MemberPick> pickedMembers = memberPickService.getPickMembers(loginFamilyId, memberId);
        return new ArrayResponse<>(
                pickedMembers
                        .stream()
                        .map(memberPick -> memberService.getMemberByMemberId(memberPick.getFromMemberId()))
                        .map(MemberResponse::of)
                        .toList()
        );
    }

    @Override
    public ArrayResponse<MemberResponse> getPickedMembers(String memberId, String loginFamilyId) {
        //내가 찌른 사람들
        List<MemberPick> pickedMembers = memberPickService.getPickedMembers(loginFamilyId, memberId);
        return new ArrayResponse<>(
                pickedMembers
                        .stream()
                        .map(memberPick -> memberService.getMemberByMemberId(memberPick.getFromMemberId()))
                        .map(MemberResponse::of)
                        .toList()
        );
    }

    private void validateMemberIdMatch(String memberId, String loginMemberId) {
        if (!loginMemberId.equals(memberId)) {
            throw new AuthorizationFailedException(UNAUTHORIZED_MEMBER_USED);
        }
    }
}
