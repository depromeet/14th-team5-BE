package com.oing.service;

import com.oing.domain.Member;
import com.oing.exception.FamilyNotFoundException;
import com.oing.exception.InvalidMemberNameLengthException;
import com.oing.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;


    @Test
    void 회원ID로_가족ID_조회_테스트() {
        // given
        Member member = new Member(
                "memberId",
                "familyId",
                LocalDate.now(),
                "name",
                "",
                "",
                LocalDateTime.now()
        );
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        // when
        String familyId = memberService.getFamilyIdByMemberId(member.getId());

        // then
        assertThat(familyId).isEqualTo(member.getFamilyId());
    }

    @Test
    void 가족이_없는_회원의_회원ID로_가족ID_조회_예외_검증() {
        // given
        Member member = new Member(
                "memberId",
                null,
                LocalDate.now(),
                "name",
                "",
                "",
                LocalDateTime.now()
        );
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        // when & then
        assertThrows(FamilyNotFoundException.class, () -> memberService.getFamilyIdByMemberId(member.getId()));
    }

    @Test
    void 가족ID로_가족_구성원_회원들_조회_테스트() {
        // given
        String familyId = "familyId";
        List<Member> members = List.of(
                new Member(
                        "memberId1",
                        familyId,
                        LocalDate.now(),
                        "name1",
                        "",
                        "",
                        LocalDateTime.now()
                ),
                new Member(
                        "memberId2",
                        familyId,
                        LocalDate.now(),
                        "name2",
                        "",
                        "",
                        LocalDateTime.now()
                )
        );
        when(memberRepository.findAllByFamilyIdAndDeletedAtIsNull(familyId)).thenReturn(members);

        // when
        List<String> familyMembersIds = memberService.getFamilyMembersIdsByFamilyId(familyId);

        // then
        assertThat(familyMembersIds).contains(members.get(0).getId(), members.get(1).getId());
    }

    @Test
    void 가족ID로_날짜_이전에_가입한_가족_구성원_회원들_조회_테스트() {
        // given
        String familyId = "familyId";
        List<Member> members = List.of(
                new Member(
                        "memberId1",
                        familyId,
                        LocalDate.now(),
                        "name1",
                        "",
                        "",
                        LocalDate.of(2024, 1, 1).atStartOfDay()
                ),
                new Member(
                        "memberId2",
                        familyId,
                        LocalDate.now(),
                        "name2",
                        "",
                        "",
                        LocalDate.of(2024, 1, 1).atStartOfDay()
                )
        );
        when(memberRepository.findAllByFamilyIdAndFamilyJoinAtBeforeAndDeletedAtIsNull(any(), any()))
                .thenReturn(members);

        // when
        List<String> familyMembersIds = memberService.getFamilyMembersIdsByFamilyIdAndJoinAtBefore(familyId, LocalDate.of(2024, 2, 1));

        // then
        assertThat(familyMembersIds).contains(members.get(0).getId(), members.get(1).getId());
    }

    @Test
    void 회원이_가족_구성원인지_확인_테스트() {
        // given
        String familyId = "familyId";
        Member member = new Member(
                "memberId",
                familyId,
                LocalDate.now(),
                "name",
                "",
                "",
                LocalDateTime.now()
        );
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        // when
        boolean isFamilyMember = memberService.isFamilyMember(member.getId(), familyId);

        // then
        assertTrue(isFamilyMember);
    }

    @Test
    void 아홉_자_초과_이름_수정_예외_검증() {
        // given
        String newName = "wrong-length-nam";
        String memberId = "1";

        // when & then
        assertThrows(InvalidMemberNameLengthException.class, () -> memberService.updateMemberName(memberId, newName));
    }

    @Test
    void 한_자_미만_이름_수정_예외_검증() {
        // given
        String newName = "";
        String memberId = "1";

        // when & then
        assertThrows(InvalidMemberNameLengthException.class, () -> memberService.updateMemberName(memberId, newName));
    }
}
