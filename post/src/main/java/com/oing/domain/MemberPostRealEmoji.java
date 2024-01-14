package com.oing.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Table(indexes = {
        @Index(name = "member_post_real_emoji_idx1", columnList = "post_id"),
        @Index(name = "member_post_real_emoji_idx2", columnList = "member_id")
})
@Entity(name = "member_post_real_emoji")
public class MemberPostRealEmoji extends BaseEntity {

    @Id
    @Column(name = "post_real_emoji_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "real_emoji_id", nullable = false)
    private MemberRealEmoji realEmoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private MemberPost post;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;
}
