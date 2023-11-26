package com.oing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PreSignedUrlRequest {
    @NotBlank
    String imageName;

    @Size(min = 1)
    long contentLength;

    @NotBlank
    String checkSum;
}
