package com.oing.util;

import com.oing.dto.response.PreSignedUrlResponse;

public interface PreSignedUrlGenerator {
    PreSignedUrlResponse getPreSignedUrl(String imageName, Long memberId);
}
