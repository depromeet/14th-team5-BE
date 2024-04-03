package com.oing.controller;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
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

import java.security.InvalidParameterException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final PostBridge postBridge;
    private final MemberPickService memberPickService;
    private final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final MemberQuitReasonService memberQuitReasonService;
    private final FCMNotificationService fcmNotificationService;

    @Override
    public PaginationResponse<FamilyMemberProfileResponse> getFamilyMembersProfiles(
            Integer page, Integer size, String loginFamilyId, String loginMemberId
    ) {
        Page<FamilyMemberProfileResponse> profilePage = memberService.findFamilyMembersProfilesByFamilyId(
                loginFamilyId, page, size
        );

        PaginationDTO<FamilyMemberProfileResponse> paginationDTO = PaginationDTO.of(profilePage);

        return PaginationResponse.of(paginationDTO, page, size);
    }

    @Override
    public MemberResponse getMember(String memberId, String loginFamilyId, String loginMemberId) {
        validateFamilyMember(memberId, loginFamilyId);
        Member member = memberService.findMemberById(memberId);

        return MemberResponse.of(member);
    }

    private void validateFamilyMember(String memberId, String loginFamilyId) {
        if (!memberService.findFamilyIdByMemberId(memberId).equals(loginFamilyId)) {
            throw new AuthorizationFailedException();
        }
    }

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getProfileImagePreSignedUrl(imageName);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberProfileImageUrl(String memberId, String loginMemberId,
                                                      UpdateMemberProfileImageUrlRequest request) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);
        String profileImgKey = preSignedUrlGenerator.extractImageKey(request.profileImageUrl());
        member.updateProfileImg(request.profileImageUrl(), profileImgKey);

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public MemberResponse deleteMemberProfileImageUrl(String memberId, String loginMemberId) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);
        member.deleteProfileImg();

        return MemberResponse.of(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMemberName(String memberId, String loginMemberId, UpdateMemberNameRequest request) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);

        validateName(request.name());
        member.updateName(request.name());

        return MemberResponse.of(member);
    }

    private void validateName(String name) {
        if (name.length() < 1 || name.length() > 9) {
            throw new InvalidParameterException();
        }
    }

    @Override
    @Transactional
    public DefaultResponse deleteMember(String memberId, String loginMemberId, QuitMemberRequest request) {
        validateMemberId(memberId, loginMemberId);
        Member member = memberService.findMemberById(memberId);
        memberService.deleteAllSocialMembersByMember(memberId);
        member.deleteMemberInfo();

        if (request != null) { //For Api Version Compatibility
            memberQuitReasonService.recordMemberQuitReason(memberId, request.reasonIds());
        }

        memberDeviceService.removeAllDevicesByMemberId(memberId);

        return DefaultResponse.ok();
    }

    @Override
    public DefaultResponse pickMember(String memberId, String loginMemberId, String loginFamilyId) {
        if (postBridge.isUploadedToday(loginFamilyId, memberId)) {
            throw new PickFailedAlreadyUploadedException();
        }
        MemberPick memberPick = memberPickService.pickMember(loginFamilyId, loginMemberId, memberId);

        Member fromMember = memberService.findMemberById(memberPick.getFromMemberId());
        Member toMember = memberService.findMemberById(memberPick.getToMemberId());
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(
                        FCMNotificationUtil.buildNotification(String.format("%s님, 살아있나요?", toMember.getName()),
                                String.format("%s님이 당신의 생존을 궁금해해요.", fromMember.getName()))
                )
                .addAllTokens(memberDeviceService.getFcmTokensByMemberId(toMember.getId()))
                .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                .build();
        fcmNotificationService.sendMulticastMessage(message);
        return DefaultResponse.ok();
    }

    @Override
    public ArrayResponse<MemberResponse> getPickMembers(String memberId, String loginFamilyId) {
        //나를 찌른 사람들
        List<MemberPick> pickedMembers = memberPickService.getPickMembers(loginFamilyId, memberId);
        return new ArrayResponse<>(
                pickedMembers
                        .stream()
                        .map(memberPick -> memberService.findMemberById(memberPick.getFromMemberId()))
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
                        .map(memberPick -> memberService.findMemberById(memberPick.getFromMemberId()))
                        .map(MemberResponse::of)
                        .toList()
        );
    }

    private void validateMemberId(String memberId, String loginMemberId) {
        if (!loginMemberId.equals(memberId)) {
            throw new AuthorizationFailedException();
        }
    }
}
