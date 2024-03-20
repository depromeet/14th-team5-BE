package com.oing.repository;

import com.oing.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, String>, PostRepositoryCustom {

    List<Post> findAllByFamilyIdAndCreatedAtBetween(String familyId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
