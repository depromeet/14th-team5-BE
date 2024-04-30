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
        if (!this.postCount.equals(o.postCount)) return -1 * this.postCount.compareTo(o.postCount);
        else {
            // 2. commentCount 내림차순
            if (!this.commentCount.equals(o.commentCount)) return -1 * this.commentCount.compareTo(o.commentCount);
            else {
                // 3. reactionCount 내림차순
                if (!this.reactionCount.equals(o.reactionCount))
                    return -1 * this.reactionCount.compareTo(o.reactionCount);
                    // 4. memberId (가입날짜) 오름차순
                else return this.memberId.compareTo(o.memberId);
            }
        }
    }
}
