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
    public void onPostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        log.info("Event of uploading Post({}) by Member({}) listened, adding family score", postCreatedEvent.getPostId(), postCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(postCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewPostScore();

        log.info("Uploading Post({}) score of Family({}) added", postCreatedEvent.getPostId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostDeletedEvent(PostDeletedEvent postDeletedEvent) {
        log.info("Event of deleting Post({}) by Member({}) listened, subtracting family score", postDeletedEvent.getPostId(), postDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(postDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewPostScore();

        log.info("Deleting Post({}) score of Family({}) subtracted", postDeletedEvent.getPostId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostCommentCreatedEvent(CommentCreatedEvent commentCreatedEvent) {
        log.info("Event of uploading Comment({}) by Member({}) listened, adding family score", commentCreatedEvent.getCommentId(), commentCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(commentCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewCommentScore();

        log.info("Uploading Comment({}) score of Family({}) added", commentCreatedEvent.getCommentId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostCommentDeletedEvent(CommentDeletedEvent commentDeletedEvent) {
        log.info("Event of Comment({}) deleted by Member({}) listened, subtracting family score", commentDeletedEvent.getCommentId(), commentDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(commentDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewCommentScore();

        log.info("Deleting Comment({}) score of Family({}) subtracted", commentDeletedEvent.getCommentId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostVoiceCommentCreatedEvent(VoiceCommentCreatedEvent voiceCommentCreatedEvent) {
        log.info("Event of uploading Voice-comment({}) by Member({}) listened, adding family score",
                voiceCommentCreatedEvent.getVoiceCommentId(), voiceCommentCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(voiceCommentCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewVoiceCommentScore();

        log.info("Uploading Voice-comment({}) score of Family({}) added", voiceCommentCreatedEvent.getVoiceCommentId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostVoiceCommentDeletedEvent(VoiceCommentCreatedEvent voiceCommentCreatedEvent) {
        log.info("Event of Voice-comment({}) deleted by Member({}) listened, subtracting family score",
                voiceCommentCreatedEvent.getVoiceCommentId(), voiceCommentCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(voiceCommentCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewVoiceCommentScore();

        log.info("Deleting Voice-comment({}) score of Family({}) subtracted", voiceCommentCreatedEvent.getVoiceCommentId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostReactionCreatedEvent(ReactionCreatedEvent reactionCreatedEvent) {
        log.info("Event of uploading Reaction({}) by Member({}) listened, adding family score", reactionCreatedEvent.getReactionId(), reactionCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(reactionCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewReactionScore();

        log.info("Uploading Reaction({}) score of Family({}) added", reactionCreatedEvent.getReactionId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostReactionDeletedEvent(ReactionDeletedEvent reactionDeletedEvent) {
        log.info("Event of uploading Reaction({}) by Member({}) listened, adding family score", reactionDeletedEvent.getReactionId(), reactionDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(reactionDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewReactionScore();

        log.info("Deleting Reaction({}) score of Family({}) subtracted", reactionDeletedEvent.getReactionId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostRealEmojiCreatedEvent(RealEmojiCreatedEvent realEmojiCreatedEvent) {
        log.info("Event of new uploading Emoji({}) by Member({}) listened, adding family score", realEmojiCreatedEvent.getRealEmojiId(), realEmojiCreatedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(realEmojiCreatedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).addNewRealEmojiScore();

        log.info("Uploading real Emoji({}) score of Family({}) added", realEmojiCreatedEvent.getRealEmojiId(), familyId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPostRealEmojiDeletedEvent(RealEmojiDeletedEvent realEmojiDeletedEvent) {
        log.info("Event of real Emoji({}) deleted by Member({}) listened, subtracting family score", realEmojiDeletedEvent.getRealEmojiId(), realEmojiDeletedEvent.getMemberId());

        String familyId = memberBridge.getFamilyIdByMemberId(realEmojiDeletedEvent.getMemberId());
        familyService.getFamilyByIdWithLock(familyId).subtractNewRealEmojiScore();

        log.info("Deleting real Emoji({}) score of Family({}) subtracted", realEmojiDeletedEvent.getRealEmojiId(), familyId);
    }
}
