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
        log.info("Event of new post uploaded by member({}) listened, adding family score", memberPostCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewPostScore();

        log.info("New post score of family({}) added", familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostDeletedEvent(MemberPostDeletedEvent memberPostDeletedEvent) {
        log.info("Event of post deleted by member({}) listened, subtracting family score", memberPostDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewPostScore();

        log.info("Deleted post score of family({}) subtracted", familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCommentCreatedEvent(MemberPostCommentCreatedEvent memberPostCommentCreatedEvent) {
        log.info("Event of new comment uploaded by member({}) listened, adding family score", memberPostCommentCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewCommentScore();

        log.info("New comment score of family({}) added", familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostCommentDeletedEvent(MemberPostCommentDeletedEvent memberPostCommentDeletedEvent) {
        log.info("Event of comment deleted by member({}) listened, subtracting family score", memberPostCommentDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewCommentScore();

        log.info("Deleted comment score of family({}) subtracted", familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostReactionCreatedEvent(MemberPostReactionCreatedEvent memberPostReactionCreatedEvent) {
        log.info("Event of new reaction uploaded by member({}) listened, adding family score", memberPostReactionCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewReactionScore();

        log.info("New reaction score of family({}) added", familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostReactionDeletedEvent(MemberPostReactionDeletedEvent memberPostReactionDeletedEvent) {
        log.info("Event of new reaction uploaded by member({}) listened, adding family score", memberPostReactionDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewReactionScore();

        log.info("Deleted reaction score of family({}) subtracted", familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostRealEmojiCreatedEvent(MemberPostRealEmojiCreatedEvent memberPostRealEmojiCreatedEvent) {
        log.info("Event of new real emoji uploaded by member({}) listened, adding family score", memberPostRealEmojiCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewRealEmojiScore();

        log.info("New real emoji score of family({}) added", familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMemberPostRealEmojiDeletedEvent(MemberPostRealEmojiDeletedEvent memberPostRealEmojiDeletedEvent) {
        log.info("Event of real emoji deleted by member({}) listened, subtracting family score", memberPostRealEmojiDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewRealEmojiScore();

        log.info("Deleted real emoji score of family({}) subtracted", familyId);
    }


}
