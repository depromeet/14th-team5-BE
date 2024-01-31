package com.oing.domain;

import com.oing.event.MemberPostReactionCreatedEvent;
import com.oing.event.MemberPostReactionDeletedEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MemberPostReactionEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MemberPostReactionEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

   @PostPersist
    public void onPostPersist(MemberPostReaction memberPostReaction) {
        applicationEventPublisher.publishEvent(new MemberPostReactionCreatedEvent(memberPostReaction, memberPostReaction.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(MemberPostReaction memberPostReaction) {
        applicationEventPublisher.publishEvent(new MemberPostReactionDeletedEvent(memberPostReaction, memberPostReaction.getMemberId()));
    }
}
