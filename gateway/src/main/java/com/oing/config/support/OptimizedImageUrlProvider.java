package com.oing.config.support;

import com.oing.util.OptimizedImageUrlGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OptimizedImageUrlProvider implements OptimizedImageUrlGenerator {

    @Value("${cloud.ncp.storage.bucket}")
    private String bucketName;

    @Value("${cloud.ncp.image-optimizer-cdn}")
    private String imageOptimizerCdnUrl;

    private static final String THUMBNAIL_OPTIMIZER_QUERY_STRING = "?type=f&w=96&h=96&quality=70&align=4&faceopt=false&anilimit=1";

    @Override
    public String getThumbnailUrlGenerator(String bucketImageUrl) {
        String imagePath = bucketImageUrl.split(bucketName)[1];

        return imageOptimizerCdnUrl + imagePath + THUMBNAIL_OPTIMIZER_QUERY_STRING;
    }
}
