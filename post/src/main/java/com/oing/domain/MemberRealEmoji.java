package com.oing.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Table(indexes = {
        @Index(name = "member_real_emoji_idx1", columnList = "member_id")
})
@Entity(name = "member_real_emoji")
public class MemberRealEmoji extends BaseAuditEntity {

    @Id
    @Column(name = "member_real_emoji_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;

    @Column(name = "family_id", columnDefinition = "CHAR(26)", nullable = false)
    private String familyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Emoji type;

    @Column(name = "real_emoji_image_url", nullable = false)
    private String realEmojiImageUrl;

    @Column(name = "real_emoji_image_key", nullable = false)
    private String realEmojiImageKey;

    public void updateRealEmoji(String realEmojiImageUrl, String realEmojiImageKey) {
        this.realEmojiImageUrl = realEmojiImageUrl;
        this.realEmojiImageKey = realEmojiImageKey;
    }
}
