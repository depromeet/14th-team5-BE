package com.oing.service;

import com.oing.domain.PostType;
import com.oing.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 3/31/24
 * Time: 5:48 PM
 */
@RequiredArgsConstructor
@Service
public class PostBridgeImpl implements PostBridge {
    private final PostRepository postRepository;

    @Override
    public boolean isUploadedToday(String familyId, String memberId) {
        LocalDate today = LocalDate.now();
        return postRepository.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, familyId, PostType.SURVIVAL, today);
    }
}
