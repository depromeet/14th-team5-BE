package com.oing.domain;

import com.oing.event.MemberPostCommentCreatedEvent;
import com.oing.event.MemberPostCommentDeletedEvent;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
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

    @PostRemove
    public void onPostRemove(MemberPostComment memberPostComment) {
        applicationEventPublisher.publishEvent(new MemberPostCommentDeletedEvent(memberPostComment, memberPostComment.getMemberId()));
    }
}
