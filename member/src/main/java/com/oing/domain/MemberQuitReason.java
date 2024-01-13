package com.oing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/13
 * Time: 11:31 PM
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Getter
@Entity(name = "member_quit_reason")
public class MemberQuitReason extends BaseEntity {
    @Id
    @Column(name = "member_id", length = 26, columnDefinition = "CHAR(26)")
    private String memberId;

    @Column(name = "reason_id", length = 255, columnDefinition = "VARCHAR(255)")
    private String reasonId;
}
