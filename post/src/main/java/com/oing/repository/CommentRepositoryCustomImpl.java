package com.oing.repository;

import com.oing.domain.Comment;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.oing.domain.QComment.comment;


@RequiredArgsConstructor
@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public QueryResults<Comment> searchPostComments(int page, int size, String postId, boolean asc) {
        return queryFactory
                .select(comment)
                .from(comment)
                .where(comment.post.id.eq(postId))
                .orderBy(asc ? comment.id.asc() : comment.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }

    @Override
    public long countMonthlyCommentByMemberId(int year, int month, String memberId) {
        return queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.memberId.eq(memberId),
                        comment.createdAt.year().eq(year),
                        comment.createdAt.month().eq(month))
                .fetchFirst();
    }
}
