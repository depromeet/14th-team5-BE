package com.oing.domain.model;

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

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "comment_cnt", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int commentCnt;

    @Column(name = "reaction_cnt", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int reactionCnt;

    @OneToMany(mappedBy = "post")
    private List<MemberPostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<MemberPostReaction> reactions = new ArrayList<>();
}
