package com.oing.config.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oing.util.OpenAIImageTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIImageTransformerImpl implements OpenAIImageTransformer {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    /**
     * 1. 이미지 분석 → 텍스트 설명 생성
     */
    @Override
    public String analyzeImageToText(byte[] imageBytes) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // GPT에 이미지 보내서 텍스트 분석
            String url = "https://api.openai.com/v1/chat/completions";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // 이미지 분석 프롬프트: 한복, 추석, 얼굴/헤어스타일 강조 등 구체적인 지시 추가
            String systemPrompt = "Crucially, all people and animals in the image **must be wearing traditional Korean Hanbok**. You are an AI assistant that describes images in detail for image generation purposes. " +
                    "Provide a very detailed and sharp description only for faces, including gender and hairstyle, " +
                    "Include Chuseok festival elements such as songpyeon, lanterns, autumn foliage, and traditional decorations, " +
                    "but do not describe them in excessive detail. " +
                    "Use soft, natural lighting without harsh reflections or artificial shine. " +
                    "Avoid overly glossy or plastic-like textures on clothing and skin. " +
                    "Colors should be warm and natural, not oversaturated. " +
                    "Generate a high-resolution image capturing the warmth, and natural atmosphere of Chuseok, " +
                    "with Hanbok appearance emphasized and the scene looking natural. Ensure your description avoids any sensitive or inappropriate content.";

            String body = "{\n" +
                    "  \"model\": \"gpt-4.1-mini\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"system\", \"content\": \"" + systemPrompt + "\"},\n" +
                    "    {\"role\": \"user\", \"content\": [{\"type\": \"text\", \"text\": \"Describe this image in detail for image generation purposes.\"}, {\"type\": \"image_url\", \"image_url\": {\"url\": \"data:image/jpeg;base64," + base64Image + "\"}}]}\n" +
                    "  ]\n" +
                    "}";

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    if (message != null && message.containsKey("content")) {
                        String description = message.get("content").toString();
                        log.info("이미지 분석 완료 - 생성된 프롬프트: {}", description);
                        return description;
                    }
                }
            }

            throw new RuntimeException("이미지 분석 실패: 유효한 응답 없음");

        } catch (Exception e) {
            log.error("이미지 분석 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 분석 실패: " + e.getMessage(), e);
        }
    }


    /**
     * 2. 텍스트 기반 이미지 생성
     */
    // generateImageFromText 수정 예시
    public byte[] generateImageFromText(String promptText) {
        try {
            log.info("이미지 생성 시작 - prompt: {}", promptText);

            String url = "https://api.openai.com/v1/images/generations";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> bodyMap = Map.of(
                    "model", "dall-e-3",
                    "prompt", promptText,
                    "n", 1,
                    "size", "1024x1024",
                    "response_format", "b64_json"
            );
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(bodyMap);

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("OpenAI API 응답 오류 - 상태 코드: {}, 응답 본문: {}",
                        response.getStatusCode(), response.getBody());
                throw new RuntimeException("이미지 생성 실패: API 상태 코드 " + response.getStatusCode());
            }

            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");

            if (data == null || data.isEmpty() || !data.get(0).containsKey("b64_json")) {
                log.error("OpenAI API 응답에 Base64 데이터 없음 - 응답 본문: {}", responseBody);
                throw new RuntimeException("이미지 생성 실패: 이미지 Base64 데이터 없음");
            }

            String base64Data = data.get(0).get("b64_json").toString();
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            log.info("이미지 생성 완료 - prompt: {}", promptText);
            return imageBytes;

        } catch (Exception e) {
            log.error("이미지 생성 실패", e);
            throw new RuntimeException("이미지 생성 실패: " + e.getMessage(), e);
        }
    }
}
