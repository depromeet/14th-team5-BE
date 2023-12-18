package com.oing.repository;

import com.oing.domain.MemberPostCountDTO;
import com.oing.domain.model.MemberPost;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberPostRepositoryCustom {

    List<MemberPost> findLatestPostOfEveryday(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate);
    List<MemberPostCountDTO> countPostsOfEveryday(List<String> memberIds, LocalDateTime startDate, LocalDateTime endDate);
}
