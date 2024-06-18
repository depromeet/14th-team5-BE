package com.oing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.oing.domain.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findFamilyMemberNamesByFamilyId(String familyId) {
        return new ArrayList<>(queryFactory.select(member.name)
                .from(member)
                .where(member.familyId.eq(familyId)
                        .and(member.deletedAt.isNull()))
                .orderBy(member.profileImgUrl.desc().nullsLast())
                .fetch());
    }

    @Override
    public List<String> findFamilyMemberProfileImgUrlsByFamilyId(String familyId) {
        return new ArrayList<>(queryFactory.select(member.profileImgUrl)
                .from(member)
                .where(member.familyId.eq(familyId)
                        .and(member.deletedAt.isNull()))
                .orderBy(member.profileImgUrl.desc().nullsLast())
                .fetch());
    }
}
