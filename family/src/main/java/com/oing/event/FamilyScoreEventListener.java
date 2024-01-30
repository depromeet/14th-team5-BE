package com.oing.event;

import com.oing.service.FamilyService;
import com.oing.service.MemberBridge;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class FamilyScoreEventListener {

    public final MemberBridge memberBridge;
    public final FamilyService familyService;

    @EventListener
    public void onMemberPostCreatedEvent(MemberPostCreatedEvent memberPostCreatedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostCreatedEvent.getMemberId());
        familyService.getFamilyById(familyId).addNewPostScore();
    }

    @EventListener
    public void onMemberPostDeletedEvent(MemberPostDeletedEvent memberPostDeletedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostDeletedEvent.getMemberId());
        familyService.getFamilyById(familyId).subtractNewPostScore();
    }

    @EventListener
    public void onMemberPostCommentCreatedEvent(MemberPostCommentCreatedEvent memberPostCommentCreatedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentCreatedEvent.getMemberId());
       familyService.getFamilyById(familyId).addNewCommentScore();
    }

    @EventListener
    public void onMemberPostCommentDeletedEvent(MemberPostCommentDeletedEvent memberPostCommentDeletedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentDeletedEvent.getMemberId());
       familyService.getFamilyById(familyId).subtractNewCommentScore();
    }

    @EventListener
    public void onMemberPostReactionCreatedEvent(MemberPostReactionCreatedEvent memberPostReactionCreatedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionCreatedEvent.getMemberId());
       familyService.getFamilyById(familyId).addNewReactionScore();
    }

    @EventListener
    public void onMemberPostReactionDeletedEvent(MemberPostReactionDeletedEvent memberPostReactionDeletedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionDeletedEvent.getMemberId());
       familyService.getFamilyById(familyId).subtractNewReactionScore();
    }

    @EventListener
    public void onMemberPostRealEmojiCreatedEvent(MemberPostRealEmojiCreatedEvent memberPostRealEmojiCreatedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiCreatedEvent.getMemberId());
        familyService.getFamilyById(familyId).addNewRealEmojiScore();
    }

    @EventListener
    public void onMemberPostRealEmojiDeletedEvent(MemberPostRealEmojiDeletedEvent memberPostRealEmojiDeletedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiDeletedEvent.getMemberId());
        familyService.getFamilyById(familyId).subtractNewRealEmojiScore();
    }


}
