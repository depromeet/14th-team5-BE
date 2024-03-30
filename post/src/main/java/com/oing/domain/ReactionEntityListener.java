package com.oing.domain;

import com.oing.event.ReactionCreatedEvent;
import com.oing.event.ReactionDeletedEvent;
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
public class ReactionEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public ReactionEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

   @PostPersist
    public void onPostPersist(Reaction reaction) {
       log.info("Reaction({}) persist listened, publishing ReactionCreatedEvent", reaction.getId());
        applicationEventPublisher.publishEvent(new ReactionCreatedEvent(reaction, reaction.getId(), reaction.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(Reaction reaction) {
        log.info("Reaction({}) removal listened, publishing ReactionDeletedEvent", reaction.getId());
        applicationEventPublisher.publishEvent(new ReactionDeletedEvent(reaction, reaction.getId(), reaction.getMemberId()));
    }
}
