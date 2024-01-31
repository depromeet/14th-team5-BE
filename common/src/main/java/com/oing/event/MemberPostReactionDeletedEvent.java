package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostReactionDeletedEvent extends ApplicationEvent {

    private final String memberId;

    public MemberPostReactionDeletedEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }
}
