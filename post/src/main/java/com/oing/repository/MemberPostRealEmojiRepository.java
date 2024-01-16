package com.oing.repository;

import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostRealEmoji;
import com.oing.domain.MemberRealEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPostRealEmojiRepository extends JpaRepository<MemberPostRealEmoji, String> {
    boolean existsByPostAndMemberIdAndRealEmoji(MemberPost post, String memberId, MemberRealEmoji emoji);
}
