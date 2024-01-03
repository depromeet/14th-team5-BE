package com.oing.repository;

import com.oing.domain.Emoji;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.oing.domain.model.QMemberPostReaction.memberPostReaction;

@RequiredArgsConstructor
public class MemberPostReactionRepositoryImpl implements MemberPostReactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> getMemberIdsByPostAndEmoji(String postId, Emoji emoji) {
        return queryFactory
                .select(memberPostReaction.memberId)
                .from(memberPostReaction)
                .where(
                        memberPostReaction.post.id.eq(postId),
                        memberPostReaction.emoji.eq(emoji)
                )
                .fetch();
    }
}
