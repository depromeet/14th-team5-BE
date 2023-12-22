package com.oing.service;

import com.oing.domain.MemberPostCountDTO;
import com.oing.domain.model.MemberPost;
import com.oing.exception.PostNotFoundException;
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
     * 멤버들이 범위 날짜 안에 올린 대표 게시물을 가져온다.
     * (대표 게시글의 기준은 당일 가장 늦게 올라온 게시글)
     * @param memberIds 조회 대상 멤버들의 ID
     * @param inclusiveStartDate 조회 시작 날짜
     * @param exclusiveEndDate 조회 종료 날짜
     */
    public List<MemberPost> findLatestPostOfEveryday(List<String> memberIds, LocalDate inclusiveStartDate, LocalDate exclusiveEndDate) {
        return memberPostRepository.findLatestPostOfEveryday(memberIds, inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay());
    }

  
    /**
     *  멤버들이 범위 날짜 안에 올린 게시글의 갯수를 가져온다.
     * @param memberIds 조회 대상 멤버들의 ID
     * @param inclusiveStartDate 조회 시작 날짜
     * @param exclusiveEndDate 조회 종료 날짜
     * @return 날짜별 게시글 갯수 DTO
     */
    public List<MemberPostCountDTO> countPostsOfEveryday(List<String> memberIds, LocalDate inclusiveStartDate, LocalDate exclusiveEndDate) {
        return memberPostRepository.countPostsOfEveryday(memberIds, inclusiveStartDate.atStartOfDay(), exclusiveEndDate.atStartOfDay());
    }


    public MemberPost findMemberPostById(String postId) {
        return memberPostRepository
                .findById(postId)
                .orElseThrow(PostNotFoundException::new);

      
    /**
     * 멤버가 해당 요일(클라이언트 기준의 오늘)에 게시글을 작성했는지 확인한다.
     * @param memberId 조회 대상 멤버들의 ID
     * @param today 조회 날짜
     * @return 오늘 회원이 작성한 글이 있는지 반환
     */
    public boolean hasUserCreatedPostToday(String memberId, LocalDate today) {
        return memberPostRepository.existsByMemberIdAndPostDate(memberId, today);
    }

      
    /**
     * 멤버가 오늘 작성한 게시물을 저장한다.
     * @param post 저장할 MemberPost 객체
     */
    public void save(MemberPost post) {
        memberPostRepository.save(post);
    }
}
