package com.oing.repository;

import com.oing.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {
    void deleteAllByPostId(String memberPostId);

    List<Comment> findByPostId(String postId);
}
