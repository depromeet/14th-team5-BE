package com.oing.domain;

import com.oing.event.MemberPostRealEmojiCreatedEvent;
import com.oing.event.MemberPostRealEmojiDeletedEvent;
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
public class MemberPostRealEmojiEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public MemberPostRealEmojiEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(MemberPostRealEmoji memberPostRealEmoji) {
        log.info("RealEmoji({}) persist listened, publishing MemberPostRealEmojiCreatedEvent", memberPostRealEmoji.getId());
        applicationEventPublisher.publishEvent(new MemberPostRealEmojiCreatedEvent(memberPostRealEmoji, memberPostRealEmoji.getId(), memberPostRealEmoji.getPost().getMemberId()));
    }

    @PostRemove
    public void onPostRemove(MemberPostRealEmoji memberPostRealEmoji) {
        log.info("RealEmoji({}) removal listened, publishing MemberPostRealEmojiDeletedEvent", memberPostRealEmoji.getId());
        applicationEventPublisher.publishEvent(new MemberPostRealEmojiDeletedEvent(memberPostRealEmoji, memberPostRealEmoji.getId(), memberPostRealEmoji.getPost().getMemberId()));
    }
}
