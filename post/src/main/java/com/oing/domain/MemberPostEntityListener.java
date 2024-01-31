package com.oing.domain;

import com.oing.event.MemberPostCreatedEvent;
import com.oing.event.MemberPostDeletedEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MemberPostEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MemberPostEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(MemberPost memberPost) {
        applicationEventPublisher.publishEvent(new MemberPostCreatedEvent(memberPost, memberPost.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(MemberPost memberPost) {
        applicationEventPublisher.publishEvent(new MemberPostDeletedEvent(memberPost, memberPost.getMemberId()));
    }
}
