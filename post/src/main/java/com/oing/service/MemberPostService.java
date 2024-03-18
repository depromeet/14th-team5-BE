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

    public List<MemberPost> findLatestPostOfEveryday(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, String familyId) {
        return memberPostRepository.findLatestPostOfEveryday(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), familyId);
    }

    public MemberPost findMemberPostById(String postId) {
        return memberPostRepository
                .findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

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

    public List<MemberPost> findAllByFamilyIdAndCreatedAtBetween(String familyId, LocalDate startDate, LocalDate endDate) {
        return memberPostRepository.findAllByFamilyIdAndCreatedAtBetween(familyId, startDate.atStartOfDay(), endDate.atStartOfDay());
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

    public boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate) {
        return memberPostRepository.existsByFamilyIdAndCreatedAt(familyId, postDate);
    }

    public boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate) {
        return memberPostRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, postDate);
    }
}
