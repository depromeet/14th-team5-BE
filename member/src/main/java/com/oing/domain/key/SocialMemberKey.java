package com.oing.domain.key;

import com.oing.domain.SocialLoginProvider;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:43 AM
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SocialMemberKey implements Serializable {
    private SocialLoginProvider provider;
    private String identifier;
}
