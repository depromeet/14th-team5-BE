package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostRealEmojiDeletedEvent extends ApplicationEvent {

    private final String memberId;

    public MemberPostRealEmojiDeletedEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }
}
