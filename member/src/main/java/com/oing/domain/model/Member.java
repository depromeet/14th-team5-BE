package com.oing.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:31 AM
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Builder
@Entity(name = "member")
public class Member extends DeletableBaseAuditEntity {
    @Id
    @Column(name = "member_id", length = 26, columnDefinition = "CHAR(26)")
    private String id;

    @Column(name = "family_id", length = 26, columnDefinition = "CHAR(26)")
    private String familyId;

    @Column(name = "day_of_birth", nullable = false)
    private LocalDate dayOfBirth;

    @Column(name = "name", nullable = false, length = 36)
    private String name;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "profile_img_key")
    private String profileImgKey;

    public void updateProfileImg(String profileImgUrl, String profileImgKey) {
        this.profileImgUrl = profileImgUrl;
        this.profileImgKey = profileImgKey;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteMemberInfo() {
        super.updateDeletedAt();
        this.name = "DeletedUser";
        this.profileImgUrl = null;
    }
}
