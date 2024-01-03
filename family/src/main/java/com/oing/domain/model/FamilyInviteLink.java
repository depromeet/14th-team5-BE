package com.oing.domain.model;

import com.oing.domain.SerializableDeepLink;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

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
public class FamilyInviteLink extends BaseEntity implements SerializableDeepLink {
    @Id
    @Column(name = "link_id", columnDefinition = "CHAR(20)", nullable = false)
    private String linkId;

    @Column(name = "family_id", columnDefinition = "CHAR(20)", nullable = false)
    private String familyId;

    @Override
    public Map<String, String> serialize() {
        return Map.of("familyId", familyId);
    }
}
