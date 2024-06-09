package com.oing.service;

import com.oing.domain.DeepLink;
import com.oing.domain.DeepLinkType;
import com.oing.exception.LinkNotValidException;
import com.oing.repository.DeepLinkRepository;
import com.oing.util.RandomStringGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/3/24
 * Time: 10:41 AM
 */
@RequiredArgsConstructor
@Service
public class DeepLinkService {

    private final DeepLinkRepository deepLinkRepository;

    @Transactional
    public String generateNewDeepLinkId() {
        //이미 존재하는 링크인지 검증
        String linkId;
        do {
            linkId = RandomStringGenerator.generateAlphanumericString(8);
        } while (deepLinkRepository.existsById(linkId));
        return linkId;
    }

    @Transactional
    public DeepLink createDeepLink(String linkId, DeepLinkType type) {
        DeepLink newDeepLink = new DeepLink(linkId, type);
        return deepLinkRepository.save(newDeepLink);
    }


    @Transactional
    public DeepLink getDeepLink(String linkId) {
        return deepLinkRepository
                .findById(linkId)
                .orElseThrow(LinkNotValidException::new);
    }

    public DeepLink getFamilyInviteLink(String linkId) {
        DeepLink deepLink = deepLinkRepository
                .findById(linkId)
                .orElseThrow(LinkNotValidException::new);

        if (!deepLink.getType().equals(DeepLinkType.FAMILY_REGISTRATION)) {
            throw new LinkNotValidException();
        }

        return deepLink;
    }
}
