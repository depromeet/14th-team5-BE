package com.oing.domain;

import com.oing.event.MemberPostReactionCreatedEvent;
import com.oing.event.MemberPostReactionDeletedEvent;
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
public class MemberPostReactionEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public MemberPostReactionEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

   @PostPersist
    public void onPostPersist(MemberPostReaction memberPostReaction) {
       log.info("Reaction({}) persist listened, publishing MemberPostReactionCreatedEvent", memberPostReaction.getId());
        applicationEventPublisher.publishEvent(new MemberPostReactionCreatedEvent(memberPostReaction, memberPostReaction.getId(), memberPostReaction.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(MemberPostReaction memberPostReaction) {
        log.info("Reaction({}) removal listened, publishing MemberPostReactionDeletedEvent", memberPostReaction.getId());
        applicationEventPublisher.publishEvent(new MemberPostReactionDeletedEvent(memberPostReaction, memberPostReaction.getId(), memberPostReaction.getMemberId()));
    }
}
