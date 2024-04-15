package com.oing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {
        @Index(name = "post_idx1", columnList = "member_id")
})
@Entity(name = "post")
@EntityListeners(PostEntityListener.class)
public class Post extends BaseAuditEntity {
    @Id
    @Column(name = "post_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;

    @Column(name = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private String familyId;

    @Column(name = "mission_id", columnDefinition = "CHAR(26)")
    private String missionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PostType type;

    @Column(name = "post_img_url", nullable = false)
    private String postImgUrl;

    @Column(name = "post_img_key", nullable = false)
    private String postImgKey;

    @Column(name = "content")
    private String content;

    @Column(name = "comment_cnt", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int commentCnt;

    @Column(name = "reaction_cnt", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int reactionCnt;

    @Column(name = "real_emoji_cnt", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int realEmojiCnt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Reaction> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<RealEmoji> realEmojis = new ArrayList<>();

    public Post(String id, String memberId, String familyId, PostType type, String postImgUrl, String postImgKey, String content) {
        validateContent(content);
        this.id = id;
        this.memberId = memberId;
        this.familyId = familyId;
        this.type = type;
        this.postImgUrl = postImgUrl;
        this.postImgKey = postImgKey;
        this.content = content;
        this.commentCnt = 0;
        this.reactionCnt = 0;
        this.realEmojiCnt = 0;
    }

    private void validateContent(String content) {
        if (content != null && (content.codePoints().count() > 8 || content.contains(" "))) {
            throw new InvalidParameterException();
        }
    }

    public void addReaction(Reaction reaction) {
        this.reactions.add(reaction);
        this.reactionCnt = this.reactions.size();
    }

    public void removeReaction(Reaction reaction) {
        this.reactions.remove(reaction);
        this.reactionCnt = this.reactions.size();
    }

    public void addRealEmoji(RealEmoji realEmoji) {
        this.realEmojis.add(realEmoji);
        this.realEmojiCnt = this.realEmojis.size();
    }

    public void removeRealEmoji(RealEmoji realEmoji) {
        this.realEmojis.remove(realEmoji);
        this.realEmojiCnt = this.realEmojis.size();
    }

    public Comment addComment(Comment comment) {
        this.comments.add(comment);
        this.commentCnt = this.comments.size();
        return comment;
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        this.commentCnt = this.comments.size();
    }
}
