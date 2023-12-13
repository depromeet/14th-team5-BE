package com.oing.util;

import com.oing.dto.response.PreSignedUrlResponse;

public interface PreSignedUrlGenerator {
    PreSignedUrlResponse getFeedPreSignedUrl(String imageName, Long memberId);
}
