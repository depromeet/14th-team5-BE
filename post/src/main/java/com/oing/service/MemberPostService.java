package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.PaginationDTO;
import com.oing.exception.PostNotFoundException;
import com.oing.repository.MemberPostRepository;
import com.oing.service.event.DeleteMemberPostEvent;
import com.querydsl.core.QueryResults;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberPostService {

    private final MemberPostRepository memberPostRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    public MemberPost save(MemberPost post) {
        return memberPostRepository.save(post);
    }


    /**
     * 멤버들이 범위 날짜 안에 올린 대표 게시물들을 가져온다.
     * (대표 게시글의 기준은 당일 가장 늦게 올라온 게시글)
     *
     * @param memberIds          조회 대상 멤버들의 ID
     * @param inclusiveStartDate 조회 시작 날짜
     * @param exclusiveEndDate   조회 종료 날짜
     * @return 데일리 대표 게시물들
     */
    public List<MemberPost> findLatestPostOfEveryday(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, String familyId) {
        return memberPostRepository.findLatestPostOfEveryday(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), familyId);
    }


    public MemberPost findMemberPostById(String postId) {
        return memberPostRepository
                .findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }


    /**
     * 멤버가 해당 요일(클라이언트 기준의 오늘)에 게시글을 작성했는지 확인한다.
     *
     * @param memberId 조회 대상 멤버들의 ID
     * @param today    조회 날짜
     * @return 오늘 회원이 작성한 글이 있는지 반환
     */
    public boolean hasUserCreatedPostToday(String memberId, String familyId, LocalDate today) {
        return memberPostRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, today);
    }

    @Transactional
    public MemberPost getMemberPostById(String postId) {
        return memberPostRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public PaginationDTO<MemberPost> searchMemberPost(int page, int size, LocalDate date, String memberId, String requesterMemberId, String familyId, boolean asc) {
        QueryResults<MemberPost> results = memberPostRepository.searchPosts(page, size, date, memberId, requesterMemberId, familyId, asc);
        int totalPage = (int) Math.ceil((double) results.getTotal() / size);
        return new PaginationDTO<>(
                totalPage,
                results.getResults()
        );
    }

    /**
     * 특정 기간 동안 특정 멤버가 올린 게시글의 갯수를 반환한다.
     *
     * @param memberIds          조회 대상 멤버들의 ID
     * @param inclusiveStartDate 조회 시작 날짜
     * @param exclusiveEndDate   조회 종료 날짜
     * @return 조회 대상인 게시글의 갯수
     */
    public long countMemberPostsByMemberIdsBetween(List<String> memberIds, LocalDate inclusiveStartDate, LocalDate exclusiveEndDate) {
        return memberPostRepository.countByMemberIdInAndCreatedAtBetween(memberIds, inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay());
    }

    @Transactional
    public void deleteMemberPostById(String postId) {
        MemberPost memberPost = memberPostRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        applicationEventPublisher.publishEvent(new DeleteMemberPostEvent(memberPost));
        memberPostRepository.delete(memberPost);
    }

    @Transactional
    public long countMonthlyPostByFamilyId(int year, int month, String familyId) {
        return memberPostRepository.countMonthlyPostByFamilyId(year, month, familyId);
    }

    public List<String> getMemberIdsPostedToday(LocalDate date) {
        return memberPostRepository.getMemberIdsPostedToday(date);
    }

    public boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate) {
        return memberPostRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, postDate);
    }
}
