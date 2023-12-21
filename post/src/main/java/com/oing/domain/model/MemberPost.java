package com.oing.domain.model;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {
        @Index(name = "member_post_idx1", columnList = "member_id")
})
@Entity(name = "member_post")
public class MemberPost extends BaseAuditEntity {
    @Id
    @Column(name = "post_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;

    @Column(name = "post_date", nullable = false)
    private LocalDate postDate;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "content")
    private String content;

    @Column(name = "comment_cnt", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int commentCnt;

    @Column(name = "reaction_cnt", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int reactionCnt;

    @OneToMany(mappedBy = "post")
    private List<MemberPostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<MemberPostReaction> reactions = new ArrayList<>();

    public MemberPost(String id, String memberId, LocalDate postDate, String imageUrl, String content) {
        validateContent(content);
        this.id = id;
        this.memberId = memberId;
        this.postDate = postDate;
        this.imageUrl = imageUrl;
        this.content = content;
        this.commentCnt = 0;
        this.reactionCnt = 0;
    }

    private void validateContent(String content) {
        if (content != null && content.length() > 8) {
            throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
