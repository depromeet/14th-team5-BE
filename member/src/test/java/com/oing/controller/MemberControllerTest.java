package com.oing.controller;

import com.oing.domain.Member;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
import com.oing.dto.response.FamilyMemberProfileResponse;
import com.oing.dto.response.MemberResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.service.MemberService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;
    @Mock
    private MemberService memberService;
    @Mock
    private AuthenticationHolder authenticationHolder;
    @Mock
    private PreSignedUrlGenerator preSignedUrlGenerator;

    private Member TEST_MEMBER1;
    private Member TEST_MEMBER2;

    @BeforeEach
    void setUp() {
        TEST_MEMBER1 = new Member("1", "1",
                LocalDate.of(2000, 7, 8), "testMember1",
                "http://test.com/test-profile.jpg", null);
        TEST_MEMBER2 = new Member("2", "1",
                LocalDate.of(2003, 7, 26), "testMember2", null, null);
    }

    @Test
    void 멤버_프로필_조회_테스트() {
        // given
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);

        // when
        MemberResponse response = memberController.getMember(TEST_MEMBER1.getId());

        // then
        assertEquals(TEST_MEMBER1.getId(), response.memberId());
        assertEquals(TEST_MEMBER1.getName(), response.name());
        assertEquals(TEST_MEMBER1.getProfileImgUrl(), response.imageUrl());
        assertEquals(TEST_MEMBER1.getFamilyId(), response.familyId());
        assertEquals(TEST_MEMBER1.getDayOfBirth(), response.dayOfBirth());
    }

    @Test
    void 가족_멤버_프로필_조회_테스트() {
        // given
        Member member1 = spy(TEST_MEMBER1);
        Member member2 = spy(TEST_MEMBER2);
        String familyId = "1";
        when(authenticationHolder.getUserId()).thenReturn("1");
        when(memberService.findFamilyIdByMemberId(anyString())).thenReturn(familyId);
        Page<FamilyMemberProfileResponse> profilePage = new PageImpl<>(Arrays.asList(
                new FamilyMemberProfileResponse(member1.getId(), member1.getName(), member1.getProfileImgUrl()),
                new FamilyMemberProfileResponse(member2.getId(), member2.getName(), member2.getProfileImgUrl())
        ));
        when(memberService.findFamilyMembersProfilesByFamilyId(familyId, 1, 5))
                .thenReturn(profilePage);

        // when
        PaginationResponse<FamilyMemberProfileResponse> response = memberController.
                getFamilyMembersProfiles(1, 5);

        // then
        assertFalse(response.hasNext());
        assertEquals(2, response.results().size());
    }

    @Test
    void 멤버_닉네임_수정_테스트() {
        // given
        String newName = "newName";
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);
        when(authenticationHolder.getUserId()).thenReturn("1");

        // when
        UpdateMemberNameRequest request = new UpdateMemberNameRequest(newName);
        memberController.updateMemberName(member.getId(), request);

        // then
        verify(member).updateName(eq(newName));
        assertEquals(newName, member.getName());
    }

    @Test
    void 아홉_자_초과_형식의_닉네임_수정_예외_테스트() {
        // given
        String newName = "wrong-length-nam";
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);
        when(authenticationHolder.getUserId()).thenReturn("1");

        // when
        UpdateMemberNameRequest request = new UpdateMemberNameRequest(newName);

        // then
        assertThrows(InvalidParameterException.class, () -> memberController.updateMemberName(member.getId(), request));
    }

    @Test
    void 한_자_미만_형식의_닉네임_수정_예외_테스트() {
        // given
        String newName = "";
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);
        when(authenticationHolder.getUserId()).thenReturn("1");

        // when
        UpdateMemberNameRequest request = new UpdateMemberNameRequest(newName);

        // then
        assertThrows(InvalidParameterException.class, () -> memberController.updateMemberName(member.getId(), request));
    }

    @Test
    void 멤버_프로필이미지_업로드_URL_요청_테스트() {
        // given
        String newProfileImage = "profile.jpg";

        // when
        PreSignedUrlRequest request = new PreSignedUrlRequest(newProfileImage);
        PreSignedUrlResponse dummyResponse = new PreSignedUrlResponse("https://test.com/presigend-request-url");
        when(preSignedUrlGenerator.getProfileImagePreSignedUrl(any())).thenReturn(dummyResponse);
        PreSignedUrlResponse response = memberController.requestPresignedUrl(request);

        // then
        assertNotNull(response.url());
    }

    @Test
    void 멤버_프로필이미지_수정_테스트() {
        // given
        String newProfileImageUrl = "http://test.com/profile.jpg";
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);
        when(authenticationHolder.getUserId()).thenReturn("1");
        when(preSignedUrlGenerator.extractImageKey(any())).thenReturn("/profile.jpg");

        // when
        UpdateMemberProfileImageUrlRequest request = new UpdateMemberProfileImageUrlRequest(newProfileImageUrl);
        memberController.updateMemberProfileImageUrl(member.getId(), request);

        // then
        verify(member).updateProfileImg(eq(newProfileImageUrl), anyString());
        assertEquals(newProfileImageUrl, member.getProfileImgUrl());
    }

    @Test
    void 멤버_탈퇴_테스트() {
        // given
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);
        when(authenticationHolder.getUserId()).thenReturn("1");

        // when
        memberController.deleteMember(member.getId());

        // then
        assertEquals("DeletedMember", member.getName());
        assertNull(member.getProfileImgUrl());
    }

    @Test
    void 잘못된_요청의_멤버_탈퇴_예외_테스트() {
        // given
        Member member = spy(TEST_MEMBER1);
        when(authenticationHolder.getUserId()).thenReturn("2");

        // then
        assertThrows(AuthorizationFailedException.class, () -> memberController.deleteMember(member.getId()));
    }
}
