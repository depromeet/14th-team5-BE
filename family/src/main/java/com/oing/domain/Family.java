package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "family")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Family extends BaseEntity {

    @Id
    @Column(name = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(name = "score", nullable = false)
    private Integer score = 0;


    public Family(String id) {
        this.id = id;
    }

    private static final int NEW_POST_SCORE = 20;
    private static final int ALL_FAMILY_MEMBERS_POSTS_UPLOADED_SCORE = 50;
    private static final int NEW_COMMENT_SCORE = 5;
    private static final int NEW_REACTION_SCORE = 1;
    private static final int NEW_REAL_EMOJI_SCORE = 3;

    public void addNewPostScore() {
        addScore(NEW_POST_SCORE);
    }

    public void subtractNewPostScore() {
        subtractScore(NEW_POST_SCORE);
    }

    public void addAllFamilyMembersPostsUploadedScore() {
        addScore(ALL_FAMILY_MEMBERS_POSTS_UPLOADED_SCORE);
    }

    public void subtractAllFamilyMembersPostsUploadedScore() {
        subtractScore(ALL_FAMILY_MEMBERS_POSTS_UPLOADED_SCORE);
    }

    public void addNewCommentScore() {
        addScore(NEW_COMMENT_SCORE);
    }

    public void subtractNewCommentScore() {
        subtractScore(NEW_COMMENT_SCORE);
    }

    public void addNewReactionScore() {
        addScore(NEW_REACTION_SCORE);
    }

    public void subtractNewReactionScore() {
        subtractScore(NEW_REACTION_SCORE);
    }

    public void addNewRealEmojiScore() {
        addScore(NEW_REAL_EMOJI_SCORE);
    }

    public void subtractNewRealEmojiScore() {
        subtractScore(NEW_REAL_EMOJI_SCORE);
    }

    private void addScore(int score) {
        this.score += score;
    }

    private void subtractScore(int score) {
        this.score -= score;
    }
}
