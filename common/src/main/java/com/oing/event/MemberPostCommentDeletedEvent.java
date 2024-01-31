package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostCommentDeletedEvent  extends ApplicationEvent {

    private final String memberId;

    public MemberPostCommentDeletedEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }
}
