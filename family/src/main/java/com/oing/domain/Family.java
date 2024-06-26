package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.InvalidParameterException;

@Entity(name = "family")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Family extends BaseEntity {

    @Id
    @Column(name = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(name = "family_name", columnDefinition = "CHAR(9)")
    private String familyName;

    @Column(name = "family_name_editor_id", columnDefinition = "CHAR(26)")
    private String familyNameEditorId;

    @Column(name = "score", nullable = false)
    private Integer score = 0;


    public Family(String id, String familyName, String familyNameEditorId) {
        this.id = id;
        this.familyName = familyName;
        this.familyNameEditorId = familyNameEditorId;
    }

    public static final int NEW_POST_SCORE = 20;
    public static final int ALL_FAMILY_MEMBERS_POSTS_UPLOADED_SCORE = 50;
    public static final int NEW_COMMENT_SCORE = 5;
    public static final int NEW_REACTION_SCORE = 1;
    public static final int NEW_REAL_EMOJI_SCORE = 3;

    public void addNewPostScore() {
        addScore(NEW_POST_SCORE);
    }

    public void subtractNewPostScore() {
        subtractScore(NEW_POST_SCORE);
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

    public void addScore(int score) {
        this.score += score;
    }

    public void subtractScore(int score) {
        this.score -= score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void updateFamilyName(String familyName, String loginFamilyId) {
        if (familyName == null) {
            this.familyName = null;
            this.familyNameEditorId = null;
        } else {
            validateFamilyName(familyName);
            this.familyName = familyName;
            this.familyNameEditorId = loginFamilyId;
        }
    }

    private void validateFamilyName(String familyName) {
        if ((familyName.codePoints().count() > 9) || familyName.isBlank()) {
            throw new InvalidParameterException();
        }
    }
}
