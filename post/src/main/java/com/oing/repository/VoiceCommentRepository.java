package com.oing.repository;

import com.oing.domain.VoiceComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceCommentRepository extends JpaRepository<VoiceComment, String>, VoiceCommentRepositoryCustom {
    List<VoiceComment> findByPostId(String postId);
}
