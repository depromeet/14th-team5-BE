package com.oing.service;

import com.oing.domain.MemberQuitReason;
import com.oing.domain.MemberQuitReasonType;
import com.oing.repository.MemberQuitReasonRepository;
import org.assertj.core.util.Lists;
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
        MemberQuitReasonType reasonId = MemberQuitReasonType.FAMILY_MEMBER_NOT_USING;
        when(memberQuitReasonRepository.saveAll(any())).thenReturn(
                Lists.list(new MemberQuitReason(
                        memberId,
                        reasonId
                ))
        );

        // when
        memberQuitReasonService.recordMemberQuitReason(memberId, Lists.list(reasonId));
        // then
        //nothing. just check no exception
    }

}
