package com.oing.domain;

import jakarta.validation.constraints.Size;

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
        @Size(min = 1, max = 9)
        String memberName,
        LocalDate dayOfBirth,
        String profileImgUrl
) {
}
