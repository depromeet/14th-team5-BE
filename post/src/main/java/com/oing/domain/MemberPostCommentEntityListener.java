package com.oing.domain;

import com.oing.event.MemberPostCommentCreatedEvent;
import jakarta.persistence.PostPersist;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MemberPostCommentEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MemberPostCommentEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(MemberPostComment memberPostComment) {
        applicationEventPublisher.publishEvent(new MemberPostCommentCreatedEvent(memberPostComment, memberPostComment.getMemberId()));
    }
}
