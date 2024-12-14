package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VoiceCommentDeletedEvent extends ApplicationEvent {

    private final String voiceCommentId;
    private final String memberId;

    public VoiceCommentDeletedEvent(Object source, String voiceCommentId, String memberId) {
        super(source);
        this.voiceCommentId = voiceCommentId;
        this.memberId = memberId;
    }
}
