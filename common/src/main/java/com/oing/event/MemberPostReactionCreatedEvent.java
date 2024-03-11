package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostReactionCreatedEvent extends ApplicationEvent {

    private final String reactionId;
    private final String memberId;

    public MemberPostReactionCreatedEvent(Object source, String reactionId, String memberId) {
        super(source);
        this.reactionId = reactionId;
        this.memberId = memberId;
    }
}
