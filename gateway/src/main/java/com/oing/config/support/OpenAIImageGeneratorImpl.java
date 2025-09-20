package com.oing.config.support;

import com.oing.util.OpenAIImageGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIImageGeneratorImpl implements OpenAIImageGenerator {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.api-url:https://api.openai.com}")
    private String apiUrl;

    @Override
    public String generateHanbokImage(String originalImageUrl) {
        String prompt = "Transform this person into wearing beautiful traditional Korean hanbok clothing " +
                "in a Chuseok (Korean Thanksgiving) autumn festival atmosphere. " +
                "Keep the person's face and facial features exactly the same. " +
                "Background should show traditional Korean autumn scenery with warm colors, " +
                "full moon, traditional Korean architecture or nature. " +
                "High quality, realistic, festive Chuseok mood.";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "dall-e-3");
        requestBody.put("prompt", prompt);
        requestBody.put("n", 1);
        requestBody.put("size", "1024x1024");
        requestBody.put("quality", "hd");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            log.info("OpenAI API 호출 시작 - 원본 이미지: {}", originalImageUrl);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    apiUrl + "/v1/ai-images/convert", entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, String>> data = (List<Map<String, String>>) responseBody.get("data");

                if (data != null && !data.isEmpty()) {
                    String generatedImageUrl = data.get(0).get("url");
                    log.info("OpenAI 이미지 생성 성공: {}", generatedImageUrl);
                    return generatedImageUrl;
                }
            }

            log.error("OpenAI API 응답이 비어있거나 올바르지 않습니다. Status: {}", response.getStatusCode());
            throw new RuntimeException("OpenAI API에서 이미지 생성에 실패했습니다.");

        } catch (Exception e) {
            log.error("OpenAI API 호출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("AI 이미지 변환에 실패했습니다: " + e.getMessage(), e);
        }
    }
}
