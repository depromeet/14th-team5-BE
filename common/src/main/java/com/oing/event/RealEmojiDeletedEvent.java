package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RealEmojiDeletedEvent extends ApplicationEvent {

    private final String realEmojiId;
    private final String memberId;

    public RealEmojiDeletedEvent(Object source, String realEmojiId, String memberId) {
        super(source);
        this.realEmojiId = realEmojiId;
        this.memberId = memberId;
    }
}
