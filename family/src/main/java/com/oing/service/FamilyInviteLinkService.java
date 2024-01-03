package com.oing.service;

import com.oing.domain.model.Family;
import com.oing.domain.model.FamilyInviteLink;
import com.oing.exception.LinkNotValidException;
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
    public String getOrCreateFamilyInviteLink(Family family) {
        //이미 생성된 링크가 있는지 검증
        FamilyInviteLink previousLink = familyInviteLinkRepository.findByFamily(family);
        if(previousLink != null) {
            return FAMILY_LINK_PREFIX + previousLink.getLinkId();
        }
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

    @Transactional
    public FamilyInviteLink getFamilyInviteLink(String linkId) {
        return familyInviteLinkRepository
                .findById(linkId)
                .orElseThrow(LinkNotValidException::new);
    }
}
