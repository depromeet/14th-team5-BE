package com.oing.service;

import com.oing.domain.Member;
import com.oing.exception.InvalidMemberNameLengthException;
import com.oing.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;


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
