package com.oing.controller;

import com.oing.domain.Member;
import com.oing.dto.request.UpdateMemberNameRequest;
import com.oing.dto.request.UpdateMemberProfileImageUrlRequest;
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
import org.springframework.test.context.ActiveProfiles;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    void setUp() {
        when(authenticationHolder.getUserId()).thenReturn("1");
        TEST_MEMBER1 = new Member("1", "1",
                LocalDate.of(2000, 7, 8), "testMember1", null, null);
    }

    @Test
    void 멤버_닉네임_수정_테스트() {
        // given
        String newName = "newName";
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);

        // when
        UpdateMemberNameRequest request = new UpdateMemberNameRequest(newName);
        memberController.updateMemberName(member.getId(), request);

        // then
        verify(member).updateName(eq(newName));
        assertEquals(newName, member.getName());
    }

    @Test
    void 잘못된_형식의_닉네임_수정_예외_테스트() {
        // given
        String newName = "wrong-length-name";
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);

        // when
        UpdateMemberNameRequest request = new UpdateMemberNameRequest(newName);

        // then
        assertThrows(InvalidParameterException.class, () -> memberController.updateMemberName(member.getId(), request));
    }

    @Test
    void 멤버_프로필이미지_수정_테스트() {
        // given
        String newProfileImage = "http://test.com/profile.jpg";
        Member member = spy(TEST_MEMBER1);
        when(memberService.findMemberById(any())).thenReturn(member);
        when(preSignedUrlGenerator.extractImageKey(any())).thenReturn("/test.jpg");

        // when
        UpdateMemberProfileImageUrlRequest request = new UpdateMemberProfileImageUrlRequest(newProfileImage);
        memberController.updateMemberProfileImageUrl(member.getId(), request);

        // then
        verify(member).updateProfileImg(eq(newProfileImage), anyString());
        assertEquals(newProfileImage, member.getProfileImgUrl());
    }
}
