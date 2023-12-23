package com.oing.service;

import com.oing.domain.model.Family;
import com.oing.domain.model.FamilyInviteLink;
import com.oing.repository.FamilyInviteLinkRepository;
import com.oing.util.RandomStringGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/21/23
 * Time: 7:18 PM
 */
@RequiredArgsConstructor
@Service
public class FamilyInviteLinkService {
    public static String FAMILY_LINK_PREFIX = "https://no5ing.kr/o/";

    private final FamilyInviteLinkRepository familyInviteLinkRepository;

    @Transactional
    public String generateFamilyInviteLink(Family family) {
        //이미 존재하는 링크인지 검증
        String linkId;
        do {
            linkId = RandomStringGenerator.generateAlphanumericString(8);
        } while(familyInviteLinkRepository.existsById(linkId));

        //링크 생성
        FamilyInviteLink inviteLink = new FamilyInviteLink(linkId, family);
        familyInviteLinkRepository.save(inviteLink);
        return FAMILY_LINK_PREFIX + linkId;
    }
}
