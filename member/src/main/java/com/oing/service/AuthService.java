package com.oing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.SocialLoginResult;
import com.oing.dto.response.AppleKeyListResponse;
import com.oing.dto.response.AppleKeyResponse;
import com.oing.dto.response.KakaoAuthResponse;
import com.oing.exception.DomainException;
import com.oing.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidParameterException;
import java.security.Key;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Objects;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:18 AM
 */
@RequiredArgsConstructor
@Service
public class AuthService {
    private final ObjectMapper objectMapper;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public SocialLoginResult authenticateFromProvider(SocialLoginProvider provider, String accessToken) {
        return switch (provider) {
            case APPLE -> authenticateFromApple(accessToken);
            case KAKAO -> authenticateFromKakao(accessToken);
            case GOOGLE -> authenticateFromGoogle(accessToken);
            default -> throw new InvalidParameterException();
        };
    }

    private SocialLoginResult authenticateFromApple(String accessToken) {
        AppleKeyResponse[] keys = retrieveAppleKeys();
        try {
            String[] tokenParts = accessToken.split("\\.");
            String headerPart = new String(Base64.getDecoder().decode(tokenParts[0]));
            JsonNode headerNode = objectMapper.readTree(headerPart);
            String kid = headerNode.get("kid").asText();

            AppleKeyResponse matchedKey = Arrays.stream(keys)
                    .filter(key -> key.kid().equals(kid))
                    .findFirst()
                    // 일치하는 키가 없음 => 만료된 토큰 or 이상한 토큰 => throw
                    .orElseThrow(InvalidParameterException::new);

            String identifier = parseIdentifierFromAppleToken(matchedKey, accessToken);
            return new SocialLoginResult(identifier);
        } catch (Exception ex) {
            throw new DomainException(ErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }

    private SocialLoginResult authenticateFromGoogle(String accessToken) {
        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(accessToken);
            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            return new SocialLoginResult(userId);
        } catch (Exception ex) {
            throw new DomainException(ErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }

    private SocialLoginResult authenticateFromKakao(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", Collections.singletonList("Bearer " + accessToken));

        ResponseEntity<KakaoAuthResponse> authResponse = restTemplate.exchange(RequestEntity
                .post("https://kapi.kakao.com/v2/user/me")
                .headers(headers)
                .build(), KakaoAuthResponse.class);

        // 인증 실패시 throw
        if (!authResponse.getStatusCode().is2xxSuccessful())
            throw new DomainException(ErrorCode.UNKNOWN_SERVER_ERROR);

        return new SocialLoginResult(Objects.requireNonNull(authResponse.getBody()).id().toString());
    }

    private AppleKeyResponse[] retrieveAppleKeys() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded;charset=utf-8"));

        ResponseEntity<AppleKeyListResponse> keyListResponse = restTemplate.exchange(RequestEntity
                .get("https://appleid.apple.com/auth/keys")
                .headers(headers)
                .build(), AppleKeyListResponse.class);

        // 키 반환 실패시 throw
        if (!keyListResponse.getStatusCode().is2xxSuccessful())
            throw new DomainException(ErrorCode.UNKNOWN_SERVER_ERROR);

        return Objects.requireNonNull(keyListResponse.getBody()).keys();
    }

    private String parseIdentifierFromAppleToken(AppleKeyResponse matchedKey, String accessToken)
            throws JsonProcessingException, ParseException, JOSEException {
        Key keyData = JWK.parse(objectMapper.writeValueAsString(matchedKey)).toRSAKey().toRSAPublicKey();
        Jws<Claims> parsedClaims = Jwts.parserBuilder()
                .setSigningKey(keyData)
                .build()
                .parseClaimsJws(accessToken);

        return parsedClaims.getBody().get("sub", String.class);
    }
}
