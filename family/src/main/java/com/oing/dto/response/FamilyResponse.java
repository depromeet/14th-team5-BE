package com.oing.dto.response;

import com.oing.domain.model.Family;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Schema(description = "가족 응답")
public record FamilyResponse(
        @Schema(description = "가족 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
        String familyId,

        @Schema(description = "가족 생성 시간", example = "2021-12-05T12:30:00.000+09:00")
        ZonedDateTime createdAt
) {
        public static FamilyResponse of(Family family) {
                return new FamilyResponse(
                        family.getId(),
                        family.getCreatedAt().atZone(ZoneId.systemDefault())
                );
        }
}
