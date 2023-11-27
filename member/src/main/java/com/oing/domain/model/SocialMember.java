package com.oing.domain.model;

import com.oing.domain.SocialLoginProvider;
import com.oing.domain.model.key.SocialMemberKey;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:32 AM
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Table(indexes = @Index(name = "social_member_idx1", columnList = "memberId"))
@IdClass(SocialMemberKey.class)
@Entity(name = "social_member")
public class SocialMember extends BaseAuditEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private SocialLoginProvider provider;

    @Id
    @Column(name = "identifier")
    private String identifier;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
