package com.oing.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member_notification_history")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {
        @Index(name = "member_notification_history_idx1", columnList = "sender_member_id"),
        @Index(name = "member_notification_history_idx2", columnList = "receiver_member_id")
})
public class MemberNotificationHistory extends BaseEntity {
    @Id
    @Column(name = "member_notification_history_id", columnDefinition = "CHAR(26)", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "aos_deep_link")
    private String aosDeepLink;

    @Column(name = "ios_deep_link")
    private String iosDeepLink;

    @Column(name = "sender_member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String senderMemberId;

    @Column(name = "receiver_member_id", columnDefinition = "CHAR(26)", nullable = false)
    private String receiverMemberId;
}
