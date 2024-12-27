package com.oing.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {
        @Index(name = "voice_comment_idx1", columnList = "post_id"),
        @Index(name = "voice_comment_idx2", columnList = "member_id")
})
@Entity(name = "voice_comment")
@EntityListeners(VoiceCommentEntityListener.class)
public class VoiceComment extends BaseComment {
    @Id
    @Column(name = "voice_comment_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String memberId;

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public String getContent() {
        return audioUrl;
    }
}
