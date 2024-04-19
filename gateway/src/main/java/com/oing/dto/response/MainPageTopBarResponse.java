package com.oing.dto.response;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 4/16/24
 * Time: 4:07 PM
 */
public record MainPageTopBarResponse(
        String memberId,
        String imageUrl,
        String noImageLetter,
        String displayName,
        Integer displayRank,
        boolean shouldShowBirthdayMark,
        boolean shouldShowPickIcon
) {
}
