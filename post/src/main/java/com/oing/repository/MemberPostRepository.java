package com.oing.repository;

import com.oing.domain.model.MemberPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPostRepository extends JpaRepository<MemberPost, String> {
}
