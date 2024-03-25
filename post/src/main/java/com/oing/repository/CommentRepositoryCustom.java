package com.oing.repository;

import com.oing.domain.Comment;
import com.querydsl.core.QueryResults;

public interface CommentRepositoryCustom {
    QueryResults<Comment> searchPostComments(int page, int size, String postId, boolean asc);
}
