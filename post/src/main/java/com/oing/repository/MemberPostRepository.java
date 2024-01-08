package com.oing.repository;

import com.oing.domain.MemberPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPostRepository extends JpaRepository<MemberPost, String>, MemberPostRepositoryCustom {
}
