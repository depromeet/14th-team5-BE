package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReactionCreatedEvent extends ApplicationEvent {

    private final String reactionId;
    private final String memberId;

    public ReactionCreatedEvent(Object source, String reactionId, String memberId) {
        super(source);
        this.reactionId = reactionId;
        this.memberId = memberId;
    }
}
