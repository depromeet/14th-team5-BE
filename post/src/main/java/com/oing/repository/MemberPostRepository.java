package com.oing.repository;

import com.oing.domain.MemberPostCountDTO;
import com.oing.domain.model.MemberPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MemberPostRepository extends JpaRepository<MemberPost, String> {

    @Query("SELECT p FROM member_post p " +
            "WHERE p.memberId IN :memberIds AND " +
                "p.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY p.createdAt HAVING p.createdAt = MAX(p.createdAt) " +
            "ORDER BY p.createdAt ASC")
    List<MemberPost> findPostByMemberIdsBetweenDateGroupByMaxCreatedAtOrderByCreatedAtAsc(List<String> memberIds, LocalDate startDate, LocalDate endDate);


    @Query("SELECT new com.oing.domain.MemberPostCountDTO(p.createdAt, COUNT(p)) FROM member_post p " +
            "WHERE p.memberId IN :memberIds AND " +
                "p.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY p.createdAt " +
            "ORDER BY p.createdAt ASC")
    List<MemberPostCountDTO> countPostsByMemberIdsBetweenDateGroupByCreatedAtOrderByCreatedAtAsc(List<String> memberIds, LocalDate startDate, LocalDate endDate);
}
