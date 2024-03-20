package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberPostRealEmojiDeletedEvent extends ApplicationEvent {

    private final String realEmojiId;
    private final String memberId;

    public MemberPostRealEmojiDeletedEvent(Object source, String realEmojiId, String memberId) {
        super(source);
        this.realEmojiId = realEmojiId;
        this.memberId = memberId;
    }
}
