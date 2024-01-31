package com.oing.event;

import com.oing.service.FamilyService;
import com.oing.service.MemberBridge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FamilyScoreEventListener {

    public final MemberBridge memberBridge;
    public final FamilyService familyService;

    // TODO: 1. BEFORE_COMMIT, 2. Async 모두 작동 안함. 원인 파악 후 리펙토링 필요.
//    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
//    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCreatedEvent(MemberPostCreatedEvent memberPostCreatedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCreatedEvent.getMemberId());
        familyService.getFamilyById(familyId).addNewPostScore();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostDeletedEvent(MemberPostDeletedEvent memberPostDeletedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostDeletedEvent.getMemberId());
        familyService.getFamilyById(familyId).subtractNewPostScore();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCommentCreatedEvent(MemberPostCommentCreatedEvent memberPostCommentCreatedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentCreatedEvent.getMemberId());
       familyService.getFamilyById(familyId).addNewCommentScore();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCommentDeletedEvent(MemberPostCommentDeletedEvent memberPostCommentDeletedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentDeletedEvent.getMemberId());
       familyService.getFamilyById(familyId).subtractNewCommentScore();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostReactionCreatedEvent(MemberPostReactionCreatedEvent memberPostReactionCreatedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionCreatedEvent.getMemberId());
       familyService.getFamilyById(familyId).addNewReactionScore();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostReactionDeletedEvent(MemberPostReactionDeletedEvent memberPostReactionDeletedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionDeletedEvent.getMemberId());
       familyService.getFamilyById(familyId).subtractNewReactionScore();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostRealEmojiCreatedEvent(MemberPostRealEmojiCreatedEvent memberPostRealEmojiCreatedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiCreatedEvent.getMemberId());
        familyService.getFamilyById(familyId).addNewRealEmojiScore();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostRealEmojiDeletedEvent(MemberPostRealEmojiDeletedEvent memberPostRealEmojiDeletedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiDeletedEvent.getMemberId());
        familyService.getFamilyById(familyId).subtractNewRealEmojiScore();
    }


}
