package com.oing.dto.dto;


import lombok.Getter;

@Getter
public class PostRankerDTO implements Comparable<PostRankerDTO> {

    String memberId;
    Long postCount;
    Long commentCount;
    Long reactionCount;

    public PostRankerDTO(String memberId, Long postCount, Long commentCount, Long reactionCount) {
        this.memberId = memberId;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.reactionCount = reactionCount;
    }

    @Override
    public int compareTo(PostRankerDTO o) {
        // 1. postCount 내림차순
        int postCountCompare = Long.compare(this.postCount, o.postCount);
        if (postCountCompare != 0) {
            return -1 * postCountCompare;
        }

        // 2. commentCount 내림차순
        int commentCountCompare = Long.compare(this.commentCount, o.commentCount);
        if (commentCountCompare != 0) {
            return -1 * commentCountCompare;
        }

        // 3. reactionCount 내림차순
        int reactionCountCompare = Long.compare(this.reactionCount, o.reactionCount);
        if (reactionCountCompare != 0) {
            return -1 * reactionCountCompare;
        }

        // 4. memberId (가입날짜) 오름차순
        return this.memberId.compareTo(o.memberId);
    }
}
