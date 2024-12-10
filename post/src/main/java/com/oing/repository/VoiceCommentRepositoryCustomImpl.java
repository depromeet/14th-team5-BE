package com.oing.repository;

import com.oing.domain.VoiceComment;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.oing.domain.QVoiceComment.voiceComment;


@RequiredArgsConstructor
@Repository
public class VoiceCommentRepositoryCustomImpl implements VoiceCommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public QueryResults<VoiceComment> searchPostVoiceComments(int page, int size, String postId, boolean asc) {
        return queryFactory
                .select(voiceComment)
                .from(voiceComment)
                .where(voiceComment.post.id.eq(postId))
                .orderBy(asc ? voiceComment.id.asc() : voiceComment.id.desc())
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetchResults();
    }

    @Override
    public long countMonthlyVoiceCommentByMemberId(int year, int month, String memberId) {
        return queryFactory
                .select(voiceComment.count())
                .from(voiceComment)
                .where(voiceComment.memberId.eq(memberId),
                        voiceComment.createdAt.year().eq(year),
                        voiceComment.createdAt.month().eq(month))
                .fetchFirst();
    }
}
