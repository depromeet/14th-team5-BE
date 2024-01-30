package com.oing.domain;

import com.oing.event.MemberPostCreatedEvent;
import jakarta.persistence.PostPersist;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MemberPostRealEmojiEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MemberPostRealEmojiEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(MemberPostRealEmoji memberPostRealEmoji) {
        applicationEventPublisher.publishEvent(new MemberPostCreatedEvent(memberPostRealEmoji, memberPostRealEmoji.getPost().getMemberId()));
    }
}
