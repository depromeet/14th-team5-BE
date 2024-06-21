package com.oing.service;

import com.oing.domain.Family;
import com.oing.repository.FamilyRepository;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FamilyServiceTest {

    @InjectMocks
    private FamilyService familyService;
    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private FamilyTopPercentageHistoryService familyTopPercentageHistoryService;
    @Mock
    private FamilyScoreBridge familyScoreBridge;
    @Mock
    private IdentityGenerator identityGenerator;

    @Test
    void 가족_이름_수정_테스트() {
        // given
        String newName = "new-name";
        String memberId = "1";
        String familyId = "1";

        // when
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(new Family(familyId, null, null)));
        Family family = familyService.updateFamilyName(familyId, memberId, newName);

        // then
        assertEquals(newName, family.getFamilyName());
        assertEquals(memberId, family.getFamilyNameEditorId());
    }

    @Test
    void 열_자_초과_가족_이름_수정_예외_검증() {
        // given
        String newName = "wrong-length-name";
        String memberId = "1";
        String familyId = "1";

        // when
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(new Family(familyId, null, null)));

        // then
        assertThrows(InvalidParameterException.class, () -> familyService.updateFamilyName(familyId, memberId, newName));
    }

    @Test
    void 공백만을_포함한_이름_수정_예외_검증() {
        // given
        String newName = " ";
        String memberId = "1";
        String familyId = "1";

        // when
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(new Family(familyId, null, null)));

        // then
        assertThrows(InvalidParameterException.class, () -> familyService.updateFamilyName(familyId, memberId, newName));
    }
}
