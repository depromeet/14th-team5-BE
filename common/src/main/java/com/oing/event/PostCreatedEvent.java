package com.oing.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostCreatedEvent extends ApplicationEvent {

    private final String postId;
    private final String memberId;

    public PostCreatedEvent(Object source, String postId, String memberId) {
        super(source);
        this.postId = postId;
        this.memberId = memberId;
    }
}
