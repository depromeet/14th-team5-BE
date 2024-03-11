package com.oing.domain;

import com.oing.event.MemberPostCreatedEvent;
import com.oing.event.MemberPostDeletedEvent;
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
public class MemberPostEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public MemberPostEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(MemberPost memberPost) {
        log.info("Post({}) persist listened, publishing MemberPostCreatedEvent", memberPost.getId());

        applicationEventPublisher.publishEvent(new MemberPostCreatedEvent(memberPost, memberPost.getId(), memberPost.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(MemberPost memberPost) {
        log.info("Post({}) removal listened, publishing MemberPostDeletedEvent", memberPost.getId());

        applicationEventPublisher.publishEvent(new MemberPostDeletedEvent(memberPost, memberPost.getId(), memberPost.getMemberId()));
    }
}
