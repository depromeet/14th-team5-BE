package com.oing.domain;

import com.oing.event.PostCreatedEvent;
import com.oing.event.PostDeletedEvent;
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
public class PostEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public PostEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(Post post) {
        log.info("Post({}) persist listened, publishing MemberPostCreatedEvent", post.getId());

        applicationEventPublisher.publishEvent(new PostCreatedEvent(post, post.getId(), post.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(Post post) {
        log.info("Post({}) removal listened, publishing MemberPostDeletedEvent", post.getId());

        applicationEventPublisher.publishEvent(new PostDeletedEvent(post, post.getId(), post.getMemberId()));
    }
}
