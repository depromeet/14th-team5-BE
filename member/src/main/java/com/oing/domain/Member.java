package com.oing.domain;

import com.oing.exception.InvalidMemberNameLengthException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.security.InvalidParameterException;
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

    @Column(name = "family_join_at")
    private LocalDateTime familyJoinAt;



    public void updateProfileImg(String profileImgUrl, String profileImgKey) {
        this.profileImgUrl = profileImgUrl;
        this.profileImgKey = profileImgKey;
    }

    public void deleteProfileImg() {
        this.profileImgUrl = null;
        this.profileImgKey = null;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteMemberInfo() {
        super.updateDeletedAt();
        this.name = "DeletedMember";
        this.profileImgUrl = null;
        this.profileImgKey = null;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;

        if(familyId == null) {
            this.familyJoinAt = null;
        } else {
            this.familyJoinAt = LocalDateTime.now();
        }
    }

    public boolean hasFamily() {
        return this.familyId != null;
    }
}
