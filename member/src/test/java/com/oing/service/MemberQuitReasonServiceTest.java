package com.oing.service;

import com.oing.domain.MemberQuitReason;
import com.oing.repository.MemberQuitReasonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberQuitReasonServiceTest {
    @InjectMocks
    private MemberQuitReasonService memberQuitReasonService;

    @Mock
    private MemberQuitReasonRepository memberQuitReasonRepository;

    @Test
    void 탈퇴_사유_저장_테스트() {
        // given
        String memberId = "memberId";
        String reasonId = "reasonId";
        when(memberQuitReasonRepository.save(any())).thenReturn(
                new MemberQuitReason(
                    memberId,
                    reasonId
                )
        );

        // when
        MemberQuitReason reason = memberQuitReasonService.recordMemberQuitReason(memberId, reasonId);

        // then
        assertEquals(reason.getReasonId(), reasonId);
        assertEquals(reason.getMemberId(), memberId);
    }

}
