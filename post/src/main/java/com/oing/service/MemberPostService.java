package com.oing.service;

import com.oing.domain.MemberPost;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostRequest;
import com.oing.exception.DuplicatePostUploadException;
import com.oing.exception.InvalidUploadTimeException;
import com.oing.exception.PostNotFoundException;
import com.oing.repository.MemberPostRepository;
import com.oing.service.event.DeleteMemberPostEvent;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import com.querydsl.core.QueryResults;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPostService {

    private final MemberPostRepository memberPostRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;


    public MemberPost createMemberPost(CreatePostRequest request, String loginMemberId, String loginFamilyId) {
        ZonedDateTime uploadTime = request.uploadTime();
        validateUserHasNotCreatedPostToday(loginMemberId, loginFamilyId, uploadTime);
        validateUploadTime(loginMemberId, uploadTime);

        MemberPost post = new MemberPost(identityGenerator.generateIdentity(), loginMemberId, loginFamilyId,
                request.imageUrl(), preSignedUrlGenerator.extractImageKey(request.imageUrl()), request.content());
        return memberPostRepository.save(post);
    }

    private void validateUserHasNotCreatedPostToday(String memberId, String familyId, ZonedDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        if (memberPostRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, today)) {
            log.warn("Member {} has already created a post today", memberId);
            throw new DuplicatePostUploadException();
        }
    }

    private void validateUploadTime(String memberId, ZonedDateTime uploadTime) {
        ZonedDateTime serverTime = ZonedDateTime.now();

        ZonedDateTime lowerBound = serverTime.minusDays(1).with(LocalTime.of(12, 0));
        ZonedDateTime upperBound = serverTime.plusDays(1).with(LocalTime.of(12, 0));

        if (uploadTime.isBefore(lowerBound) || uploadTime.isAfter(upperBound)) {
            log.warn("Member {} is attempting to upload a post at an invalid time", memberId);
            throw new InvalidUploadTimeException();
        }
    }

    public List<MemberPost> findLatestPostOfEveryday(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, String familyId) {
        return memberPostRepository.findLatestPostOfEveryday(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), familyId);
    }

    public MemberPost getMemberPostById(String postId) {
        return memberPostRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

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

    public long countMonthlyPostByFamilyId(int year, int month, String familyId) {
        return memberPostRepository.countMonthlyPostByFamilyId(year, month, familyId);
    }

    public List<String> findMemberIdsPostedToday(LocalDate date) {
        return memberPostRepository.getMemberIdsPostedToday(date);
    }

    public boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate) {
        return memberPostRepository.existsByFamilyIdAndCreatedAt(familyId, postDate);
    }

    public boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate) {
        return memberPostRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, postDate);
    }
}
