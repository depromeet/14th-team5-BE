package com.oing.repository;

import com.oing.domain.MemberPostComment;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.oing.domain.QMemberPostComment.memberPostComment;

@RequiredArgsConstructor
@Repository
public class MemberPostCommentRepositoryCustomImpl implements MemberPostCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public QueryResults<MemberPostComment> searchPostComments(int page, int size, String postId, boolean asc) {
        return queryFactory
                .select(memberPostComment)
                .from(memberPostComment)
                .where(memberPostComment.post.id.eq(postId))
                .orderBy(asc ? memberPostComment.id.asc() : memberPostComment.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }
}
