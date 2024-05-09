package com.oing.repository;

public interface ReactionRepositoryCustom {

    long countMonthlyReactionByMemberId(int year, int month, String memberId);
}
