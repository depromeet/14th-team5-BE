package com.oing.domain.model.key;

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
public class MemberDeviceKey implements Serializable {
    private String memberId;
    private String fcmToken;
}
