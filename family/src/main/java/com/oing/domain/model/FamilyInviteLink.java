package com.oing.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/21/23
 * Time: 7:14 PM
 */
@Entity(name = "family_invite_link")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class FamilyInviteLink extends BaseEntity {
    @Id
    @Column(name = "link_id", columnDefinition = "CHAR(20)", nullable = false)
    private String linkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;
}
