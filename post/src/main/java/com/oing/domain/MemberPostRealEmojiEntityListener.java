package com.oing.domain;

import com.oing.event.MemberPostCreatedEvent;
import com.oing.event.MemberPostDeletedEvent;
import com.oing.event.MemberPostRealEmojiCreatedEvent;
import com.oing.event.MemberPostRealEmojiDeletedEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
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
        applicationEventPublisher.publishEvent(new MemberPostRealEmojiCreatedEvent(memberPostRealEmoji, memberPostRealEmoji.getPost().getMemberId()));
    }

    @PostRemove
    public void onPostRemove(MemberPostRealEmoji memberPostRealEmoji) {
        applicationEventPublisher.publishEvent(new MemberPostRealEmojiDeletedEvent(memberPostRealEmoji, memberPostRealEmoji.getPost().getMemberId()));
    }
}
