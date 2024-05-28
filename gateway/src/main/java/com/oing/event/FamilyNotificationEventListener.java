package com.oing.event;

import com.google.firebase.messaging.MulticastMessage;
import com.oing.domain.Comment;
import com.oing.domain.Member;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.service.FCMNotificationService;
import com.oing.service.MemberDeviceService;
import com.oing.service.MemberService;
import com.oing.service.PostService;
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
    private final PostService memberPostService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        if (postCreatedEvent.getSource() instanceof Post post) {
            Member author = memberService.getMemberByMemberId(post.getMemberId());
            HashSet<String> targetFcmTokens = new HashSet<>();
            String familyId = post.getFamilyId();
            List<String> familyMemberIds = memberService.getFamilyMembersIdsByFamilyId(familyId);
            for (String familyMemberId : familyMemberIds) {
                if(post.getMemberId().equals(familyMemberId)) continue; //게시물 작성자는 발송 X
                targetFcmTokens.addAll(memberDeviceService.getFcmTokensByMemberId(familyMemberId));
            }

            if(targetFcmTokens.isEmpty()) return;
            if(post.getType() == PostType.SURVIVAL) {
                MulticastMessage multicastMessage = MulticastMessage.builder()
                        .setNotification(
                                FCMNotificationUtil.buildNotification("삐삐",
                                        String.format("%s님이 생존신고를 완료했어요.", author.getName()))
                        )
                        .putData("aosDeepLink", "post/view/" + post.getId())
                        .putData("iosDeepLink", "post/view/" + post.getId() + "?openComment=false&dateOfPost="
                                + post.getCreatedAt().toLocalDate().toString())
                        .addAllTokens(targetFcmTokens)
                        .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                        .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                        .build();
                fcmNotificationService.sendMulticastMessage(multicastMessage);

                if(memberPostService.isNewPostMadeMissionUnlocked(familyId)) {
                    sendMissionUnlockedMessages(familyMemberIds);
                }
            }
        }
    }

    private void sendMissionUnlockedMessages(List<String> familyMemberIds) {
        // 방금 막 미션이 언락 되었다면..
        HashSet<String> missionTargetFcmTokens = new HashSet<>();
        for (String familyMemberId : familyMemberIds) {
            missionTargetFcmTokens.addAll(memberDeviceService.getFcmTokensByMemberId(familyMemberId));
        }

        MulticastMessage missionUnlockedMessage = MulticastMessage.builder()
                .setNotification(
                        FCMNotificationUtil.buildNotification("열쇠를 획득해 미션 잠금이 해제되었어요!",
                                "사진 한 장을 더 찍을 수 있어요.")
                )
                .addAllTokens(missionTargetFcmTokens)
                .setApnsConfig(FCMNotificationUtil.buildApnsConfig())
                .setAndroidConfig(FCMNotificationUtil.buildAndroidConfig())
                .build();
        fcmNotificationService.sendMulticastMessage(missionUnlockedMessage);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPostCommentCreatedEvent(CommentCreatedEvent commentCreatedEvent) {
        if (commentCreatedEvent.getSource() instanceof Comment comment) {
            Post sourcePost = comment.getPost();
            String postAuthorId = sourcePost.getMemberId(); //게시물 작성자 ID
            Member author = memberService.getMemberByMemberId(comment.getMemberId()); //댓글 작성자

            if (!postAuthorId.equals(comment.getMemberId())) { //내가 내 게시물에 단 댓글이 아니라면
                List<String> targetFcmTokens = memberDeviceService.getFcmTokensByMemberId(postAuthorId);
                if(!targetFcmTokens.isEmpty()) {
                    MulticastMessage multicastMessage = MulticastMessage.builder()
                            .setNotification(
                                    FCMNotificationUtil.buildNotification(
                                            String.format("%s님이 내 피드에 남긴 댓글", author.getName()),
                                            String.format("\"%s\"", comment.getContent()))
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
                    sourcePost.getComments().stream().map(Comment::getMemberId).collect(Collectors.toSet());
            relatedMemberIds.remove(comment.getMemberId()); // 댓글 단 사람은 제외
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
                                    String.format("\"%s\"", comment.getContent()))
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
