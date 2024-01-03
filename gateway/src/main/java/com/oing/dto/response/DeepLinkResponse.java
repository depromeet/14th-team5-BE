package com.oing.dto.response;

import com.oing.domain.DeepLinkType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Schema(description = "딥링크 응답")
public class DeepLinkResponse {
    @Parameter(description = "링크 ID", example = "ab3fde93ee")
    private String linkId;

    @Parameter(description = "링크 URL", example = "https://no5ing.com/o/ab3fde93ee")
    private String url;

    @Parameter(description = "링크 타입", example = "FAMILY_REGISTRATION")
    private DeepLinkType type;

    @Parameter(description = "링크 상세 정보", example = "{'familyId': '01HGW2N7EHJVJ4CJ999RRS2E97'}")
    private Map<String, String> details;
}
