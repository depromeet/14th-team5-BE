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
    public void onMemberPostCommentCreatedEvent(MemberPostCommentCreatedEvent memberPostCommentCreatedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostCommentCreatedEvent.getMemberId());
       familyService.getFamilyById(familyId).addNewCommentScore();
    }

    @EventListener
    public void onMemberPostReactionCreatedEvent(MemberPostReactionCreatedEvent memberPostReactionCreatedEvent) {
       String familyId = memberBridge.getFamilyIdByMemberId(memberPostReactionCreatedEvent.getMemberId());
       familyService.getFamilyById(familyId).addNewReactionScore();
    }

    @EventListener
    public void onMemberPostRealEmojiCreatedEvent(MemberPostRealEmojiCreatedEvent memberPostRealEmojiCreatedEvent) {
        String familyId = memberBridge.getFamilyIdByMemberId(memberPostRealEmojiCreatedEvent.getMemberId());
        familyService.getFamilyById(familyId).addNewRealEmojiScore();
    }
}
