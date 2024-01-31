package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostDeletedEvent extends ApplicationEvent {

    private final String memberId;

    public MemberPostDeletedEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }
}
