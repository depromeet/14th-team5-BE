package com.oing.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Table(indexes = {
        @Index(name = "real_emoji_idx1", columnList = "post_id"),
        @Index(name = "real_emoji_idx2", columnList = "member_id")
})
@Entity(name = "real_emoji")
@EntityListeners(RealEmojiEntityListener.class)
public class RealEmoji extends BaseEntity {

    @Id
    @Column(name = "real_emoji_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_real_emoji_id", nullable = false)
    private MemberRealEmoji realEmoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;
}
