package com.oing.dto.response;

import java.time.LocalDate;

public record DayEventResponse(
        LocalDate date,
        Boolean allFamilyMembersUploaded
) {
}
