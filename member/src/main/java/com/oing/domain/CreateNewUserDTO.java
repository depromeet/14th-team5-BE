package com.oing.domain;

import java.time.LocalDate;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 12:04 PM
 */
public record CreateNewUserDTO(
        SocialLoginProvider provider,
        String identifier,
        String memberName,
        LocalDate dayOfBirth,
        String profileImgUrl
) {
}
