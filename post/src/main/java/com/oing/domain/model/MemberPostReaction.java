package com.oing.domain.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {
        @Index(name = "member_post_reaction_idx1", columnList = "post_id"),
        @Index(name = "member_post_reaction_idx2", columnList = "member_id")
})
@Entity(name = "member_post_reaction")
public class MemberPostReaction extends BaseEntity {
    @Id
    @Column(name = "reaction_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private MemberPost post;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;

    @Column(name = "ascii", nullable = false)
    private String ascii;
}
