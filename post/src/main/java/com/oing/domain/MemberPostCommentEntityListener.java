package com.oing.domain;

import com.oing.event.MemberPostCommentCreatedEvent;
import com.oing.event.MemberPostCommentDeletedEvent;
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
public class MemberPostCommentEntityListener {

    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    public MemberPostCommentEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostPersist
    public void onPostPersist(MemberPostComment memberPostComment) {
        log.info("Comment({}) persist listened, publishing MemberPostCommentCreatedEvent", memberPostComment.getId());
        applicationEventPublisher.publishEvent(new MemberPostCommentCreatedEvent(memberPostComment, memberPostComment.getId(), memberPostComment.getMemberId()));
    }

    @PostRemove
    public void onPostRemove(MemberPostComment memberPostComment) {
        log.info("Comment({}) removal listened, publishing MemberPostCommentDeletedEvent", memberPostComment.getId());
        applicationEventPublisher.publishEvent(new MemberPostCommentDeletedEvent(memberPostComment, memberPostComment.getId(), memberPostComment.getMemberId()));
    }
}
