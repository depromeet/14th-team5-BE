package com.oing.controller;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostCommentRequest;
import com.oing.dto.request.UpdatePostCommentRequest;
import com.oing.dto.response.DefaultResponse;
import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostCommentResponse;
import com.oing.exception.AuthorizationFailedException;
import com.oing.restapi.MemberPostCommentApi;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostCommentService;
import com.oing.service.MemberPostService;
import com.oing.util.IdentityGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberPostCommentController implements MemberPostCommentApi {
    private final IdentityGenerator identityGenerator;
    private final MemberPostService memberPostService;
    private final MemberPostCommentService memberPostCommentService;
    private final MemberBridge memberBridge;

    /**
     * 게시물의 댓글을 생성합니다
     * @param postId 게시물 ID
     * @param request 댓글 생성 요청
     * @return 생성된 댓글
     * @throws AuthorizationFailedException 내 가족이 올린 게시물이 아닌 경우
     */
    @Transactional
    @Override
    public PostCommentResponse createPostComment(String postId, CreatePostCommentRequest request, String loginMemberId) {
        log.info("Member {} is trying to create post comment", loginMemberId);
        MemberPost memberPost = memberPostService.getMemberPostById(postId);

        // 내 가족의 게시물인지 검증
        if (!memberBridge.isInSameFamily(loginMemberId, memberPost.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}",
                    loginMemberId, postId);
            throw new AuthorizationFailedException();
        }

        MemberPostComment memberPostComment = new MemberPostComment(
                identityGenerator.generateIdentity(),
                memberPost,
                loginMemberId,
                request.content()
        );
        MemberPostComment savedComment = memberPostCommentService.savePostComment(memberPostComment);
        MemberPostComment addedComment = memberPost.addComment(savedComment);
        log.info("Member {} has created post comment {}", loginMemberId, savedComment.getId());

        return PostCommentResponse.from(addedComment);
    }

    /**
     * 게시물의 댓글을 삭제합니다
     * @param postId 게시물 ID
     * @param commentId 댓글 ID
     * @return 삭제 결과
     * @throws AuthorizationFailedException 내가 작성한 댓글이 아닌 경우
     * @throws com.oing.exception.MemberPostCommentNotFoundException 댓글이 존재하지 않거나 게시물ID와 댓글ID가 일치하지 않는 경우
     */
    @Transactional
    @Override
    public DefaultResponse deletePostComment(String postId, String commentId, String loginMemberId) {
        log.info("Member {} is trying to delete post comment {}", loginMemberId, commentId);
        MemberPost memberPost = memberPostService.getMemberPostById(postId);
        MemberPostComment memberPostComment = memberPostCommentService.getMemberPostComment(postId, commentId);

        //내가 작성한 댓글인지 권한 검증
        if (!memberPostComment.getMemberId().equals(loginMemberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }

        memberPostCommentService.deletePostComment(memberPostComment);
        memberPost.removeComment(memberPostComment);
        log.info("Member {} has deleted post comment {}", loginMemberId, commentId);

        return DefaultResponse.ok();
    }

    /**
     * 게시물의 댓글을 수정합니다
     * @param postId 게시물 ID
     * @param commentId 댓글 ID
     * @param request 댓글 수정 요청
     * @return 수정된 댓글
     * @throws AuthorizationFailedException 내가 작성한 댓글이 아닌 경우
     * @throws com.oing.exception.MemberPostCommentNotFoundException 댓글이 존재하지 않거나 게시물ID와 댓글ID가 일치하지 않는 경우
     */
    @Transactional
    @Override
    public PostCommentResponse updatePostComment(String postId, String commentId, UpdatePostCommentRequest request,
                                                 String loginMemberId) {
        log.info("Member {} is trying to update post comment {}", loginMemberId, commentId);
        MemberPostComment memberPostComment = memberPostCommentService.getMemberPostComment(postId, commentId);

        //내가 작성한 댓글인지 권한 검증
        if (!memberPostComment.getMemberId().equals(loginMemberId)) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }

        memberPostComment.setContent(request.content());
        MemberPostComment savedMemberPostComment = memberPostCommentService
                .savePostComment(memberPostComment);
        log.info("Member {} has updated post comment {}", loginMemberId, commentId);

        return PostCommentResponse.from(savedMemberPostComment);
    }

    /**
     * 게시물의 댓글 목록을 조회합니다
     * @param postId 게시물 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sort 정렬 방식 (오름차순/내림차순)
     * @return 댓글 목록
     */
    @Transactional
    @Override
    public PaginationResponse<PostCommentResponse> getPostComments(String postId, Integer page, Integer size, String sort,
                                                                   String loginMemberId) {
        MemberPost memberPost = memberPostService.getMemberPostById(postId);
        if (!memberBridge.isInSameFamily(loginMemberId, memberPost.getMemberId())) {
            log.warn("Unauthorized access attempt: Member {} is attempting comment operation on post {}", loginMemberId, postId);
            throw new AuthorizationFailedException();
        }

        PaginationDTO<MemberPostComment> fetchResult = memberPostCommentService.searchPostComments(
                page, size, postId, sort == null || sort.equalsIgnoreCase("ASC")
        );

        return PaginationResponse
                .of(fetchResult, page, size)
                .map(PostCommentResponse::from);
    }
}
