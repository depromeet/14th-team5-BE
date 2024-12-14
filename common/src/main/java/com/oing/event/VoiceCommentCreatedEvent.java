package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VoiceCommentCreatedEvent extends ApplicationEvent {

        private final String voiceCommentId;
        private final String memberId;

        public VoiceCommentCreatedEvent(Object source, String voiceCommentId, String memberId) {
            super(source);
            this.voiceCommentId = voiceCommentId;
            this.memberId = memberId;
        }
}
