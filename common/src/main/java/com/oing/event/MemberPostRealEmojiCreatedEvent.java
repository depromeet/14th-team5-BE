package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostRealEmojiCreatedEvent extends ApplicationEvent {

        private final String memberId;

        public MemberPostRealEmojiCreatedEvent(Object source, String memberId) {
            super(source);
            this.memberId = memberId;
        }
}
