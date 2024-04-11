package com.oing.service;

import com.oing.domain.PaginationDTO;
import com.oing.domain.Post;
import com.oing.domain.Type;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.DuplicatePostUploadException;
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

import java.security.InvalidParameterException;
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

    @Transactional
    public PreSignedUrlResponse requestPresignedUrl(String loginMemberId, String imageName) {
        log.info("Member {} is trying to request post Pre-Signed URL", loginMemberId);

        PreSignedUrlResponse response = preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
        log.info("Post Pre-Signed URL has been generated for member {}: {}", loginMemberId, response.url());
        return response;
    }

    @Transactional
    public Post createMemberPost(CreatePostRequest request, String type, String loginMemberId, String loginFamilyId) {
        if (type.equals("feed")) {
            return createFeedPost(request, loginMemberId, loginFamilyId);
        } else if (type.equals("mission")) {
            return createMissionPost(request, loginMemberId, loginFamilyId);
        } else {
            throw new InvalidParameterException();
        }
    }

    public Post createFeedPost(CreatePostRequest request, String loginMemberId, String loginFamilyId) {
        ZonedDateTime uploadTime = request.uploadTime();
        validateUserHasNotCreatedPostToday(loginMemberId, loginFamilyId, uploadTime);
        validateUploadTime(loginMemberId, uploadTime);

        Post post = new Post(identityGenerator.generateIdentity(), loginMemberId, loginFamilyId, Type.FEED,
                request.imageUrl(), preSignedUrlGenerator.extractImageKey(request.imageUrl()), request.content());
        return postRepository.save(post);
    }

    public Post createMissionPost(CreatePostRequest request, String loginMemberId, String loginFamilyId) {
        ZonedDateTime uploadTime = request.uploadTime();
        validateUploadTime(loginMemberId, uploadTime);
        // TODO: 미션 게시물 업로드 가능한 사용자인지 검증하는 로직 필요
        Post post = new Post(identityGenerator.generateIdentity(), loginMemberId, loginFamilyId, Type.MISSION,
                request.imageUrl(), preSignedUrlGenerator.extractImageKey(request.imageUrl()), request.content());

        return null;
    }

    private void validateUserHasNotCreatedPostToday(String memberId, String familyId, ZonedDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        if (postRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, today)) {
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

    public List<Post> findLatestPostOfEveryday(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, String familyId) {
        return postRepository.findLatestPostOfEveryday(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), familyId);
    }

    public Post getMemberPostById(String postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public PaginationDTO<Post> searchMemberPost(int page, int size, LocalDate date, String memberId, String requesterMemberId,
                                                String familyId, boolean asc, String type) {
        Type postType = Type.fromString(type);
        if (postType.equals(Type.FEED)) {
            QueryResults<Post> results = postRepository.searchPosts(page, size, date, memberId, requesterMemberId, familyId, asc, postType);
            int totalPage = (int) Math.ceil((double) results.getTotal() / size);
            return new PaginationDTO<>(
                    totalPage,
                    results.getResults()
            );
        } else {
            // TODO: mission type으로 응답을 모킹하기가 힘들어 구현 전까지는 feed type으로 응답합니다
            QueryResults<Post> results = postRepository.searchPosts(page, size, date, memberId, requesterMemberId, familyId, asc, Type.FEED);
            int totalPage = (int) Math.ceil((double) results.getTotal() / size);
            return new PaginationDTO<>(
                    totalPage,
                    results.getResults()
            );
        }
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

    public boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate) {
        return postRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, postDate);
    }
}
