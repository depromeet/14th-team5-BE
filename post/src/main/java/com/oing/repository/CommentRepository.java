package com.oing.repository;

import com.oing.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {
    void deleteAllByPostId(String memberPostId);
}
