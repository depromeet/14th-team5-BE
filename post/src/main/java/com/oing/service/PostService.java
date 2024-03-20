package com.oing.service;

import com.oing.domain.Post;
import com.oing.domain.PaginationDTO;
import com.oing.exception.PostNotFoundException;
import com.oing.repository.PostRepository;
import com.oing.service.event.DeletePostEvent;
import com.querydsl.core.QueryResults;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> findLatestPostOfEveryday(LocalDate inclusiveStartDate, LocalDate exclusiveEndDate, String familyId) {
        return postRepository.findLatestPostOfEveryday(inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay(), familyId);
    }

    public Post findMemberPostById(String postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    public boolean hasUserCreatedPostToday(String memberId, String familyId, LocalDate today) {
        return postRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, today);
    }

    @Transactional
    public Post getMemberPostById(String postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public PaginationDTO<Post> searchMemberPost(int page, int size, LocalDate date, String memberId, String requesterMemberId, String familyId, boolean asc) {
        QueryResults<Post> results = postRepository.searchPosts(page, size, date, memberId, requesterMemberId, familyId, asc);
        int totalPage = (int) Math.ceil((double) results.getTotal() / size);
        return new PaginationDTO<>(
                totalPage,
                results.getResults()
        );
    }

    public List<Post> findAllByFamilyIdAndCreatedAtBetween(String familyId, LocalDate startDate, LocalDate endDate) {
        return postRepository.findAllByFamilyIdAndCreatedAtBetween(familyId, startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    @Transactional
    public void deleteMemberPostById(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        applicationEventPublisher.publishEvent(new DeletePostEvent(post));
        postRepository.delete(post);
    }

    @Transactional
    public long countMonthlyPostByFamilyId(int year, int month, String familyId) {
        return postRepository.countMonthlyPostByFamilyId(year, month, familyId);
    }

    public List<String> getMemberIdsPostedToday(LocalDate date) {
        return postRepository.getMemberIdsPostedToday(date);
    }

    public boolean existsByFamilyIdAndCreatedAt(String familyId, LocalDate postDate) {
        return postRepository.existsByFamilyIdAndCreatedAt(familyId, postDate);
    }

    public boolean existsByMemberIdAndFamilyIdAndCreatedAt(String memberId, String familyId, LocalDate postDate) {
        return postRepository.existsByMemberIdAndFamilyIdAndCreatedAt(memberId, familyId, postDate);
    }
}
