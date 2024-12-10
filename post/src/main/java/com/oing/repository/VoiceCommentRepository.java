package com.oing.repository;

import com.oing.domain.VoiceComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceCommentRepository extends JpaRepository<VoiceComment, String> {
}
