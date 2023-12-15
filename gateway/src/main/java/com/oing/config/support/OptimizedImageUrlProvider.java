package com.oing.config.support;

import com.oing.util.OptimizedImageUrlGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * NCP Image Optimizer를 사용하여 이미지 URL을 최적화한다.
 * docs: https://guide.ncloud-docs.com/docs/media-media-3-1
 * console: https://console.ncloud.com/image-optimizer/statistics
 *
 * !! 비용 모니터링 필수
 * !! 인프라 이전 시, 직접 구현해야 함
 */

@Component
public class OptimizedImageUrlProvider implements OptimizedImageUrlGenerator {

    @Value("${cloud.ncp.storage.bucket}")
    private String bucketName;

    @Value("${cloud.ncp.image-optimizer-cdn}")
    private String imageOptimizerCdnUrl;

    private static final String THUMBNAIL_OPTIMIZER_QUERY_STRING = "?type=f&w=96&h=96&quality=70&align=4&faceopt=false&anilimit=1";


    /**
     * 96x96 썸네일 이미지 URL 생성
     * @param bucketImageUrl 원본 이미지 URL
     * @return 썸네일 이미지 URL
     */
    @Override
    public String getThumbnailUrlGenerator(String bucketImageUrl) {
        String imagePath = bucketImageUrl.split(bucketName)[1];

        return imageOptimizerCdnUrl + imagePath + THUMBNAIL_OPTIMIZER_QUERY_STRING;
    }
}
