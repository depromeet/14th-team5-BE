package com.oing.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "member_post")
public class MemberPost extends BaseEntity {
    @Id
    @Column(name = "post_id", length = 26, columnDefinition = "CHAR(26)")
    private String id;

    @Column(name = "member_id", length = 26, columnDefinition = "CHAR(26)")
    private String memberId;

    @Column(name = "post_date", nullable = false)
    private LocalDate postDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "comment_cnt", nullable = false)
    private int commentCnt;

    @Column(name = "reaction_cnt", nullable = false)
    private int reactionCnt;
}
