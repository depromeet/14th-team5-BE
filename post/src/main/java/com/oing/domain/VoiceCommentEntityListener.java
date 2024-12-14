package com.oing.domain;

import com.oing.event.CommentCreatedEvent;
import com.oing.event.CommentDeletedEvent;
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
public class VoiceCommentEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public VoiceCommentEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(VoiceComment voiceComment) {
        log.info("Voice-comment({}) persist listened, publishing VoiceCommentCreatedEvent", voiceComment.getId());
        applicationEventPublisher.publishEvent(new CommentCreatedEvent(voiceComment, voiceComment.getId(), voiceComment.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(VoiceComment voiceComment) {
        log.info("Voice-comment({}) removal listened, publishing VoiceCommentDeletedEvent", voiceComment.getId());
        applicationEventPublisher.publishEvent(new CommentDeletedEvent(voiceComment, voiceComment.getId(), voiceComment.getMemberId()));
    }
}
