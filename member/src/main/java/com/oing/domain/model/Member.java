package com.oing.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class Member extends BaseAuditEntityWithDelete {
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

    public void updateProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteMemberInfo() {
        super.updateDeletedAt(LocalDateTime.now());
        this.name = "DeletedUser";
        this.profileImgUrl = null;
    }
}
