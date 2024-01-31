package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostCommentCreatedEvent extends ApplicationEvent {

        private final String memberId;

        public MemberPostCommentCreatedEvent(Object source, String memberId) {
            super(source);
            this.memberId = memberId;
        }
}
