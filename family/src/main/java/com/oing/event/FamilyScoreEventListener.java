package com.oing.event;

import com.oing.service.FamilyService;
import com.oing.service.MemberBridge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class FamilyScoreEventListener {

    public final MemberBridge memberBridge;
    public final FamilyService familyService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCreatedEvent(MemberPostCreatedEvent memberPostCreatedEvent) {
        log.info("Event of uploading Post({}) by Member({}) listened, adding family score", memberPostCreatedEvent.getPostId(), memberPostCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewPostScore();

        log.info("Uploading Post({}) score of Family({}) added", memberPostCreatedEvent.getPostId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostDeletedEvent(MemberPostDeletedEvent memberPostDeletedEvent) {
        log.info("Event of deleting Post({}) by Member({}) listened, subtracting family score", memberPostDeletedEvent.getPostId(), memberPostDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewPostScore();

        log.info("Deleting Post({}) score of Family({}) subtracted", memberPostDeletedEvent.getPostId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCommentCreatedEvent(MemberPostCommentCreatedEvent memberPostCommentCreatedEvent) {
        log.info("Event of uploading Comment({}) by Member({}) listened, adding family score", memberPostCommentCreatedEvent.getCommentId(), memberPostCommentCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewCommentScore();

        log.info("Uploading Comment({}) score of Family({}) added", memberPostCommentCreatedEvent.getCommentId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCommentDeletedEvent(MemberPostCommentDeletedEvent memberPostCommentDeletedEvent) {
        log.info("Event of Comment({}) deleted by Member({}) listened, subtracting family score", memberPostCommentDeletedEvent.getCommentId(), memberPostCommentDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewCommentScore();

        log.info("Deleting Comment({}) score of Family({}) subtracted", memberPostCommentDeletedEvent.getCommentId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostReactionCreatedEvent(MemberPostReactionCreatedEvent memberPostReactionCreatedEvent) {
        log.info("Event of uploading Reaction({}) by Member({}) listened, adding family score", memberPostReactionCreatedEvent.getReactionId(), memberPostReactionCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewReactionScore();

        log.info("Uploading Reaction({}) score of Family({}) added", memberPostReactionCreatedEvent.getReactionId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostReactionDeletedEvent(MemberPostReactionDeletedEvent memberPostReactionDeletedEvent) {
        log.info("Event of uploading Reaction({}) by Member({}) listened, adding family score", memberPostReactionDeletedEvent.getReactionId(), memberPostReactionDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewReactionScore();

        log.info("Deleting Reaction({}) score of Family({}) subtracted", memberPostReactionDeletedEvent.getReactionId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostRealEmojiCreatedEvent(MemberPostRealEmojiCreatedEvent memberPostRealEmojiCreatedEvent) {
        log.info("Event of new uploading Emoji({}) by Member({}) listened, adding family score", memberPostRealEmojiCreatedEvent.getRealEmojiId(), memberPostRealEmojiCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewRealEmojiScore();

        log.info("Uploading real Emoji({}) score of Family({}) added", memberPostRealEmojiCreatedEvent.getRealEmojiId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostRealEmojiDeletedEvent(MemberPostRealEmojiDeletedEvent memberPostRealEmojiDeletedEvent) {
        log.info("Event of real Emoji({}) deleted by Member({}) listened, subtracting family score", memberPostRealEmojiDeletedEvent.getRealEmojiId(), memberPostRealEmojiDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewRealEmojiScore();

        log.info("Deleting real Emoji({}) score of Family({}) subtracted", memberPostRealEmojiDeletedEvent.getRealEmojiId(), familyId);
    }


}
