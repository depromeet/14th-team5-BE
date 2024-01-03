package com.oing.repository;

import com.oing.domain.Emoji;

import java.util.List;

public interface MemberPostReactionRepositoryCustom {

    List<String> getMemberIdsByPostAndEmoji(String postId, Emoji emoji);
}
