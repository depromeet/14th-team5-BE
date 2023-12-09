package com.oing.domain.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {
        @Index(name = "member_post_comment_idx1", columnList = "post_id"),
        @Index(name = "member_post_comment_idx2", columnList = "member_id")
})
@Entity(name = "member_post_comment")
public class MemberPostComment extends BaseAuditEntity {
    @Id
    @Column(name = "comment_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private MemberPost post;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;

    @Column(name = "comment", nullable = false)
    private String comment;
}
