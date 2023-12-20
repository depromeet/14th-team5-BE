package com.oing.domain.model;

import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
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
public class Member extends BaseAuditEntity {
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
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name.length()<2 || name.length()>10) {
            throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
