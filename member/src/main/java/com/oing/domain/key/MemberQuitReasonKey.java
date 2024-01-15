package com.oing.domain.key;

import com.oing.domain.MemberQuitReasonType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2024/01/02
 * Time: 11:43 AM
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MemberQuitReasonKey implements Serializable {
    private String memberId;
    private MemberQuitReasonType reasonId;
}
