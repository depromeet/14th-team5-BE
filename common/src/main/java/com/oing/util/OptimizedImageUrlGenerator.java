package com.oing.util;

public interface OptimizedImageUrlGenerator {
    String getThumbnailUrlGenerator(String bucketImageUrl);

    String getKBImageUrlGenerator(String bucketImageUrl);
}

