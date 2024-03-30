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
public class CommentEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public CommentEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(Comment comment) {
        log.info("Comment({}) persist listened, publishing CommentCreatedEvent", comment.getId());
        applicationEventPublisher.publishEvent(new CommentCreatedEvent(comment, comment.getId(), comment.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(Comment comment) {
        log.info("Comment({}) removal listened, publishing MemberPostCommentDeletedEvent", comment.getId());
        applicationEventPublisher.publishEvent(new CommentDeletedEvent(comment, comment.getId(), comment.getMemberId()));
    }
}
