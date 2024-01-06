package com.oing.service;

import com.oing.domain.FamilyInviteLink;
import com.oing.exception.LinkNotValidException;
import com.oing.repository.FamilyInviteLinkRepository;
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
public class FamilyInviteLinkService implements DeepLinkDetailService<FamilyInviteLink> {
    private final FamilyInviteLinkRepository familyInviteLinkRepository;

    @Transactional
    @Override
    public FamilyInviteLink storeDeepLinkDetails(FamilyInviteLink details) {
        return familyInviteLinkRepository.save(details);
    }

    @Override
    public FamilyInviteLink findPriorDeepLinkDetails(FamilyInviteLink details) {
        return familyInviteLinkRepository.findByFamilyId(details.getFamilyId());
    }

    @Override
    public FamilyInviteLink retrieveDeepLinkDetails(String linkId) {
        return familyInviteLinkRepository
                .findById(linkId)
                .orElseThrow(LinkNotValidException::new);
    }
}
