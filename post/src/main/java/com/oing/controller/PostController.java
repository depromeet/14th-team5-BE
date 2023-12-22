package com.oing.controller;


import com.oing.domain.model.MemberPost;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostResponse;
import com.oing.dto.response.PreSignedUrlResponse;
import com.oing.exception.DuplicatePostUploadException;
import com.oing.exception.InvalidUploadTimeException;
import com.oing.restapi.PostApi;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:24 PM
 */
@RequiredArgsConstructor
@Controller
public class PostController implements PostApi {

    private final AuthenticationHolder authenticationHolder;
    private final IdentityGenerator identityGenerator;
    private final PreSignedUrlGenerator preSignedUrlGenerator;
    private final MemberPostService memberPostService;

    @Override
    public PreSignedUrlResponse requestPresignedUrl(PreSignedUrlRequest request) {
        String imageName = request.imageName();
        return preSignedUrlGenerator.getFeedPreSignedUrl(imageName);
    }

    @Override
    public PaginationResponse<PostResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date, String memberId, String sort) {
        if (page > 5) return new PaginationResponse<>(page, 5, size, false, List.of());
        if(memberId != null) {
            return new PaginationResponse<>(page, 5, size, false, List.of(
                    new PostResponse(
                            "01HGW2N7EHJVJ4CJ999RRS2E",
                            memberId,
                            0,
                            0,
                            "https://picsum.photos/200/300?random=00",
                            "즐거운 하루~",
                            ZonedDateTime.now()
                    )
            ));
        }

        String postIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String writerIdBase = "01HGW2N7EHJVJ4CJ888RRS2E";

        List<PostResponse> mockResponses = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < size; i++) {
            int currentIndex = i + ((page - 1) * size);
            String suffix = String.format("%02d", currentIndex);
            mockResponses.add(
                    new PostResponse(
                            postIdBase + suffix,
                            writerIdBase + suffix,
                            random.nextInt(5),
                            random.nextInt(5),
                            "https://picsum.photos/200/300?random=" + currentIndex,
                            "hi",
                           ZonedDateTime.now().minusSeconds(currentIndex * 30L)
                    )
            );
        }

        return new PaginationResponse<>(page, 5, size, 5 > page, mockResponses);
    }

    @Transactional
    @Override
    public PostResponse createPost(CreatePostRequest request) {
        String memberId = authenticationHolder.getUserId();
        String postId = identityGenerator.generateIdentity();
        ZonedDateTime uploadTime = request.uploadTime();
        LocalDateTime uploadLocalDateTime = request.uploadTime().toLocalDateTime();
        LocalDate uploadDate = extractLocalDate(uploadTime);

        validateUserHasNotCreatedPostToday(memberId, uploadLocalDateTime);
        validateUploadTime(uploadLocalDateTime);

        MemberPost post = new MemberPost(postId, memberId, uploadDate, request.imageUrl(), request.content());
        MemberPost savedPost = memberPostService.save(post);
        ZonedDateTime createdAt = convertToZonedDateTime(savedPost.getCreatedAt());

        return new PostResponse(savedPost.getId(), savedPost.getMemberId(), 0, 0,
                savedPost.getImageUrl(), savedPost.getContent(), createdAt);
    }

    private LocalDate extractLocalDate(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDate();
    }

    private void validateUserHasNotCreatedPostToday(String memberId, LocalDateTime uploadTime) {
        LocalDate today = uploadTime.toLocalDate();
        if (memberPostService.hasUserCreatedPostToday(memberId, today)) {
            throw new DuplicatePostUploadException();
        }
    }

    /**
     * 업로드 시간이 허용 가능한 범위 내에 있는지 검증합니다.
     * 범위는 서버의 로컬 시간을 기준으로 전 날의 오후 12시부터 다음 날의 오후 12시까지로 정의됩니다.
     *
     * @param uploadTime 검증할 업로드 시간입니다.
     * @throws InvalidUploadTimeException 업로드 시간이 허용 가능한 범위를 벗어난 경우 발생하는 예외입니다.
     */
    private void validateUploadTime(LocalDateTime uploadTime) {
        LocalDateTime serverTime = LocalDateTime.now();

        LocalDateTime lowerBound = serverTime.minusDays(1).with(LocalTime.of(12, 0));
        LocalDateTime upperBound = serverTime.plusDays(1).with(LocalTime.of(12, 0));

        if (uploadTime.isBefore(lowerBound) || uploadTime.isAfter(upperBound)) {
            throw new InvalidUploadTimeException();
        }
    }

    private ZonedDateTime convertToZonedDateTime(LocalDateTime localDateTime) {
        ZoneId serverTimeZone = ZoneId.of("Asia/Seoul");
        return localDateTime.atZone(serverTimeZone);
    }

    @Override
    public PostResponse getPost(String postId) {
        return new PostResponse(
                postId,
                "01HGW2N7EHJVJ4CJ888RRS2E",
                0,
                0,
                "https://picsum.photos/200/300?random=00",
                "즐거운 하루~",
                ZonedDateTime.now()
        );
    }
}
