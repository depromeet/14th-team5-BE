package com.oing.domain;

import com.oing.event.RealEmojiCreatedEvent;
import com.oing.event.RealEmojiDeletedEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
public class RealEmojiEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public RealEmojiEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(RealEmoji realEmoji) {
        log.info("RealEmoji({}) persist listened, publishing RealEmojiCreatedEvent", realEmoji.getId());
        applicationEventPublisher.publishEvent(new RealEmojiCreatedEvent(realEmoji, realEmoji.getId(), realEmoji.getPost().getMemberId()));
    }

    @PostRemove
    public void onPostRemove(RealEmoji realEmoji) {
        log.info("RealEmoji({}) removal listened, publishing RealEmojiDeletedEvent", realEmoji.getId());
        applicationEventPublisher.publishEvent(new RealEmojiDeletedEvent(realEmoji, realEmoji.getId(), realEmoji.getPost().getMemberId()));
    }
}
