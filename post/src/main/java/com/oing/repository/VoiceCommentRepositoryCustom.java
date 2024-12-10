package com.oing.repository;

import com.oing.domain.VoiceComment;
import com.querydsl.core.QueryResults;

public interface VoiceCommentRepositoryCustom {
    QueryResults<VoiceComment> searchPostVoiceComments(int page, int size, String postId, boolean asc);

    long countMonthlyVoiceCommentByMemberId(int year, int month, String memberId);
}
