package com.oing.repository;

import com.oing.domain.MemberPostComment;
import com.querydsl.core.QueryResults;

public interface MemberPostCommentRepositoryCustom {
    QueryResults<MemberPostComment> searchPostComments(int page, int size, String postId, boolean asc);
}
