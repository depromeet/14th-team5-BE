package com.oing.service;

import com.oing.domain.Member;
import com.oing.exception.MemberNotFoundException;
import com.oing.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberBridgeImplTest {

    @InjectMocks
    private MemberBridgeImpl memberBridge;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 멤버_생일여부_조회정상_테스트_생일() {
        // given
        String memberId = "member1";
        Member member = Member.builder()
                .id(memberId)
                .dayOfBirth(LocalDate.now())
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        boolean result = memberBridge.isBirthDayMember(memberId);

        // then
        assertTrue(result);
    }

    @Test
    void 멤버_생일여부_조회정상_테스트_생일아님() {
        // given
        String memberId = "member1";
        Member member = Member.builder()
                .id(memberId)
                .dayOfBirth(LocalDate.now().minusDays(1))
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        boolean result = memberBridge.isBirthDayMember(memberId);

        // then
        assertFalse(result);
    }

    @Test
    void 멤버_생일여부_조회실패_테스트_멤버가존재하지않음() {
        // given
        String memberId = "member1";

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(
                MemberNotFoundException.class,
                () -> memberBridge.isBirthDayMember(memberId)
        );
    }

    @Test
    void 멤버_프로필사진_조회정상_테스트() {
        // given
        String memberId = "member1";
        String profileImgUrl = "http://example.com/profile.jpg";
        Member member = Member.builder()
                .id(memberId)
                .profileImgUrl(profileImgUrl)
                .build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        String result = memberBridge.getMemberProfileImgUrlByMemberId(memberId);

        // then
        assertEquals(profileImgUrl, result);
    }

    @Test
    void 멤버_프로필사진_조회실패_테스트_멤버가존재하지않음() {
        // given
        String memberId = "member1";
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(
                MemberNotFoundException.class,
                () -> memberBridge.getMemberProfileImgUrlByMemberId(memberId)
        );
    }
}
