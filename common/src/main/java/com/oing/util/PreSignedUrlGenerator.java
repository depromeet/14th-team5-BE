package com.oing.util;

import com.oing.dto.response.PreSignedUrlResponse;

public interface PreSignedUrlGenerator {
    PreSignedUrlResponse getFeedPreSignedUrl(String imageName);

    PreSignedUrlResponse getProfileImagePreSignedUrl(String imageName);

    void deleteImageByPath(String imageUrl);
}
