package com.oing.event;

import com.google.firebase.messaging.MulticastMessage;
import com.oing.domain.Member;
import com.oing.domain.MemberPost;
import com.oing.domain.MemberPostComment;
import com.oing.service.FCMNotificationService;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberService;
import com.oing.util.FCMNotificationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FamilyNotificationEventListener {
    public final MemberService memberService;
    private final MemberDeviceService memberDeviceService;
    private final FCMNotificationService fcmNotificationService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMemberPostCreatedEvent(MemberPostCreatedEvent memberPostCreatedEvent) {
        if (memberPostCreatedEvent.getSource() instanceof MemberPost memberPost) {
            Member author = memberService.findMemberById(memberPost.getMemberId());
            HashSet<String> targetFcmTokens = new HashSet<>();
            String familyId = memberPost.getFamilyId();
            List<String> familyMemberIds = memberService.findFamilyMembersIdsByFamilyId(familyId);
            for (String familyMemberId : familyMemberIds) {
                if(memberPost.getMemberId().equals(familyMemberId)) continue; //게시물 작성자는 발송 X
                targetFcmTokens.addAll(memberDeviceService.getFcmTokensByMemberId(familyMemberId));
            }

            if(targetFcmTokens.isEmpty()) return;
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .setNotification(
                            FCMNotificationUtil.buildNotification("삐삐",
                                    String.format("%s님이 생존신고를 완료했어요.", author.getName()))
                    )
                    .putData("aosDeepLink", "post/view/" + memberPost.getId())
                    .putData("iosDeepLink", "post/view/" + memberPost.getId() + "?openComment=false&dateOfPost="
                            + memberPost.getCreatedAt().toLocalDate().toString())
                    .addAllTokens(targetFcmTokens)
                    .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                    .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                    .build();
            fcmNotificationService.sendMulticastMessage(multicastMessage);
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMemberPostCommentCreatedEvent(MemberPostCommentCreatedEvent memberPostCommentCreatedEvent) {
        if (memberPostCommentCreatedEvent.getSource() instanceof MemberPostComment memberPostComment) {
            MemberPost sourcePost = memberPostComment.getPost();
            String postAuthorId = sourcePost.getMemberId(); //게시물 작성자 ID
            Member author = memberService.findMemberById(memberPostComment.getMemberId()); //댓글 작성자

            if (!postAuthorId.equals(memberPostComment.getMemberId())) { //내가 내 게시물에 단 댓글이 아니라면
                List<String> targetFcmTokens = memberDeviceService.getFcmTokensByMemberId(postAuthorId);
                if(!targetFcmTokens.isEmpty()) {
                    MulticastMessage multicastMessage = MulticastMessage.builder()
                            .setNotification(
                                    FCMNotificationUtil.buildNotification(
                                            String.format("%s님이 내 피드에 남긴 댓글", author.getName()),
                                            String.format("\"%s\"", memberPostComment.getComment()))
                            )
                            .putData("aosDeepLink", "post/view/" + sourcePost.getId() + "?openComment=true")
                            .putData("iosDeepLink", "post/view/" + sourcePost.getId() + "?openComment=true&dateOfPost="
                                    + sourcePost.getCreatedAt().toLocalDate().toString())
                            .addAllTokens(targetFcmTokens)
                            .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                            .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                            .build();
                    fcmNotificationService.sendMulticastMessage(multicastMessage);
                }
            }

            Set<String> relatedMemberIds =
                    sourcePost.getComments().stream().map(MemberPostComment::getMemberId).collect(Collectors.toSet());
            relatedMemberIds.remove(memberPostComment.getMemberId()); // 댓글 단 사람은 제외
            relatedMemberIds.remove(postAuthorId); // 게시물 작성자도 제외

            HashSet<String> targetFcmTokens = new HashSet<>();
            for (String relatedMemberId : relatedMemberIds) {
                targetFcmTokens.addAll(memberDeviceService.getFcmTokensByMemberId(relatedMemberId));
            }

            if (targetFcmTokens.isEmpty()) return;
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .setNotification(
                            FCMNotificationUtil.buildNotification(
                                    String.format("%s님의 댓글", author.getName()),
                                    String.format("\"%s\"", memberPostComment.getComment()))
                    )
                    .putData("aosDeepLink", "post/view/" + sourcePost.getId() + "?openComment=true")
                    .putData("iosDeepLink", "post/view/" + sourcePost.getId() + "?openComment=true&dateOfPost="
                            + sourcePost.getCreatedAt().toLocalDate().toString())
                    .addAllTokens(targetFcmTokens)
                    .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                    .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                    .build();
            fcmNotificationService.sendMulticastMessage(multicastMessage);
        }
    }
}
