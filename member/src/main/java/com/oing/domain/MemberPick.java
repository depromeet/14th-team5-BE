package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/04/02
 * Time: 11:31 AM
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Entity(name = "member_pick")
public class MemberPick extends BaseEntity {
    @Id
    @Column(name = "pick_id", length = 26, columnDefinition = "CHAR(26)")
    private String pickId;

    @Column(name = "family_id", length = 26, columnDefinition = "CHAR(26)")
    private String familyId;

    @Column(name = "from_member_id", length = 26, columnDefinition = "CHAR(26)")
    private String fromMemberId;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "to_member_id", columnDefinition = "CHAR(26)")
    private String toMemberId;
}
