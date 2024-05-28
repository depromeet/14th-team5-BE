package com.oing.service;

import com.oing.domain.PaginationDTO;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.DuplicateMissionPostUploadException;
import com.oing.exception.DuplicateSurvivalPostUploadException;
import com.oing.exception.InvalidUploadTimeException;
import com.oing.exception.PostNotFoundException;
import com.oing.repository.PostRepository;
import com.oing.service.event.DeletePostEvent;
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
public class PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MissionBridge missionBridge;

    @Transactional
    public PreSignedUrlResponse requestPresignedUrl(String loginMemberId, String imageName) {
        log.info("Member {} is trying to request post Pre-Signed URL", loginMemberId);

        PreSignedUrlResponse response = preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
        log.info("Post Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());
        return response;
    }

    @Transactional
    public Post createMemberPost(CreatePostRequest request, PostType type, String loginMemberId, String loginFamilyId) {
        return switch (type) {
            case SURVIVAL -> createSurvivalPost(request, loginMemberId, loginFamilyId);
            case MISSION -> createMissionPost(request, loginMemberId, loginFamilyId);
        };
    }

    public Post createSurvivalPost(CreatePostRequest request, String loginMemberId, String loginFamilyId) {
        ZonedDateTime uploadTime = request.uploadTime();
        validateUserHasNotCreatedPostToday(loginMemberId, loginFamilyId, PostType.SURVIVAL, uploadTime);
        validateUploadTime(loginMemberId, uploadTime);

        Post post = new Post(identityGenerator.generateIdentity(), loginMemberId, loginFamilyId, PostType.SURVIVAL,
                request.imageUrl(), preSignedUrlGenerator.extractImageKey(request.imageUrl()), request.content());
        return postRepository.save(post);
    }

    public Post createMissionPost(CreatePostRequest request, String loginMemberId, String loginFamilyId) {
        ZonedDateTime uploadTime = request.uploadTime();
        validateUserHasNotCreatedPostToday(loginMemberId, loginFamilyId, PostType.MISSION, uploadTime);
        validateUploadTime(loginMemberId, uploadTime);
        String missionId = missionBridge.getTodayMissionId();

        Post post = new Post(identityGenerator.generateIdentity(), loginMemberId, loginFamilyId, missionId, PostType.MISSION,
                request.imageUrl(), preSignedUrlGenerator.extractImageKey(request.imageUrl()), request.content());
        return postRepository.save(post);
    }

    private void validateUserHasNotCreatedPostToday(String memberId, String familyId, PostType type, ZonedDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        switch (type) {
            case SURVIVAL -> validateUserHasNotCreatedSurvivalPostToday(memberId, familyId, today);
            case MISSION -> validateUserHasNotCreatedMissionPostToday(memberId, familyId, today);
        }
    }

    private void validateUserHasNotCreatedSurvivalPostToday(String memberId, String familyId, LocalDate today) {
        if (postRepository.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, familyId, PostType.SURVIVAL, today)) {
            log.warn("Member {} has already created a survival post today {}", memberId, today);
            throw new DuplicateSurvivalPostUploadException();
        }
    }

    private void validateUserHasNotCreatedMissionPostToday(String memberId, String familyId, LocalDate today) {
        if (postRepository.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, familyId, PostType.MISSION, today)) {
            log.warn("Member {} has already created a mission post today {}", memberId, today);
            throw new DuplicateMissionPostUploadException();
        }
    }

    private void validateUploadTime(String memberId, ZonedDateTime uploadTime) {
        ZonedDateTime serverTime = ZonedDateTime.now();

        ZonedDateTime lowerBound = serverTime.minusDays(1).with(LocalTime.of(10, 0));
        ZonedDateTime upperBound = serverTime.plusDays(1).with(LocalTime.of(10, 0));

        if (uploadTime.isBefore(lowerBound) || uploadTime.isAfter(upperBound)) {
            log.warn("Member {} is attempting to upload a post at an invalid time", memberId);
            throw new InvalidUploadTimeException();
        }
    }

    public List<Post> findLatestPostOfEveryday(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, String familyId) {
        return postRepository.findLatestPostOfEveryday(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), familyId);
    }

    public List<Post> findOldestPostOfEveryday(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, String familyId) {
        return postRepository.findOldestPostOfEveryday(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), familyId);
    }

    public Post getMemberPostById(String postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public Post findLatestPost(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, PostType postType, String loginFamilyId) {
        return postRepository.findLatestPost(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), postType, loginFamilyId);
    }

    public PaginationDTO<Post> searchMemberPost(int page, int size, LocalDate date, String memberId, String requesterMemberId,
                                                String familyId, boolean asc, PostType type) {
        QueryResults<Post> results = null;
        int totalPage = 0;

        switch (type) {
            case SURVIVAL -> {
                results = postRepository.searchPosts(page, size, date, memberId, requesterMemberId, familyId, asc, type);
                totalPage = (int) Math.ceil((double) results.getTotal() / size);
            }
            case MISSION -> {
                results = postRepository.searchPosts(page, size, date, memberId, requesterMemberId, familyId, asc, type);
                totalPage = (int) Math.ceil((double) results.getTotal() / size);
            }
        }
        return new PaginationDTO<>(totalPage, results.getResults());
    }

    public List<Post> findAllByFamilyIdAndCreatedAtBetween(String familyId, LocalDate startDate, LocalDate endDate) {
        return postRepository.findAllByFamilyIdAndCreatedAtBetween(familyId, startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    @Transactional
    public void deleteMemberPostById(String postId) {
        Post memberPost = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        applicationEventPublisher.publishEvent(new DeletePostEvent(memberPost));
        postRepository.delete(memberPost);
    }

    public long countMonthlyPostByFamilyId(int year, int month, String familyId) {
        return postRepository.countMonthlyPostByFamilyId(year, month, familyId);
    }

    public List<String> findMemberIdsPostedToday(LocalDate date) {
        return postRepository.getMemberIdsPostedToday(date);
    }

    public boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate) {
        return postRepository.existsByFamilyIdAndCreatedAt(familyId, postDate);
    }

    public boolean existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(String memberId, String familyId, PostType type, LocalDate postDate) {
        return postRepository.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, familyId, type, postDate);
    }

    public boolean isCreatedSurvivalPostByMajority(String familyId) {
        int totalFamilyMembers = postRepository.countFamilyMembersByFamilyIdAtYesterday(familyId);
        int survivalPostCount = postRepository.countTodaySurvivalPostsByFamilyId(familyId);

        return survivalPostCount >= totalFamilyMembers / 2;
    }

    public boolean isNewPostMadeMissionUnlocked(String familyId) {
        int totalFamilyMembers = postRepository.countFamilyMembersByFamilyIdAtYesterday(familyId);
        int survivalPostCount = postRepository.countTodaySurvivalPostsByFamilyId(familyId);
        return ((survivalPostCount - 1) < totalFamilyMembers / 2) // 방금 글 올리기 전에는 조건 만족 X but,
                && (survivalPostCount >= totalFamilyMembers / 2); //올리고 나서는 조건 만족
    }

    public int calculateRemainingSurvivalPostCountUntilMissionUnlocked(String familyId) {
        int familyMemberCount = postRepository.countFamilyMembersByFamilyIdAtYesterday(familyId);
        int requiredSurvivalPostCount = familyMemberCount / 2;
        int todaySurvivalPostCount = postRepository.countTodaySurvivalPostsByFamilyId(familyId);

        return Math.max(requiredSurvivalPostCount - todaySurvivalPostCount, 0);
    }

    public long countMonthlySurvivalPostByMemberId(LocalDate date, String memberId) {
        int year = date.getYear();
        int month = date.getMonthValue();

        return postRepository.countMonthlySurvivalPostByMemberId(year, month, memberId);
    }

    public boolean isCreatedSurvivalPostToday(String memberId, String familyId) {
        LocalDate today = ZonedDateTime.now().toLocalDate();
        return postRepository.existsByMemberIdAndFamilyIdAndTypeAndCreatedAt(memberId, familyId, PostType.SURVIVAL, today);
    }
}
