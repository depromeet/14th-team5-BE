package com.oing.domain.model;

import com.oing.domain.BaseEntity;
import com.oing.domain.model.key.MemberDeviceKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/02
 * Time: 11:31 PM
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@IdClass(MemberDeviceKey.class)
@Getter
@Entity(name = "member_device")
public class MemberDevice extends BaseEntity {
    @Id
    @Column(name = "member_id", length = 26, columnDefinition = "CHAR(26)")
    private String memberId;

    @Column(name = "fcm_token", columnDefinition = "VARCHAR(255)")
    private String fcmToken;
}
