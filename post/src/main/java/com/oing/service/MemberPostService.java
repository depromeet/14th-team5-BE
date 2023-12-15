package com.oing.service;

import com.oing.domain.MemberPostCountDTO;
import com.oing.domain.model.MemberPost;
import com.oing.repository.MemberPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberPostService {

    private final MemberPostRepository memberPostRepository;


    /**
     * 범위 날짜 안에 가족들이 업로드한 게시물 중 대표(가장 최신) 게시물을 가져온다.
     */
    public List<MemberPost> findLatestFamilyPostOfEveryday(List<String> family, LocalDate startDate, LocalDate endDate) {
        return memberPostRepository.findLatestFamilyPostOfEveryday(family, startDate, endDate);
    }

    
    /**
     *  범위 날짜 안에 가족들이 업로드한 게시물의 수를 가져온다.
     */
    public List<MemberPostCountDTO> countFamilyPostsOfEveryday(List<String> family, LocalDate startDate, LocalDate endDate) {
        return memberPostRepository.countFamilyPostsOfEveryday(family, startDate, endDate);
    }
}
