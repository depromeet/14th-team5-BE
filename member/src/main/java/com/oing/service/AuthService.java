package com.oing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.SocialLoginResult;
import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.dto.response.AppleKeyListResponse;
import com.oing.dto.response.AppleKeyResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.security.interfaces.RSAKey;
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

    public SocialLoginResult authenticateFromProvider(SocialLoginProvider provider, String accessToken) {
        return switch (provider) {
            case APPLE -> authenticateFromApple(accessToken);
            default -> throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }

    private SocialLoginResult authenticateFromApple(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-type", Collections.singletonList("application/x-www-form-urlencoded;charset=utf-8"));

        ResponseEntity<AppleKeyListResponse> keyListResponse = restTemplate.exchange(RequestEntity
                .get("https://appleid.apple.com/auth/keys")
                .headers(headers)
                .build(), AppleKeyListResponse.class);

        if(!keyListResponse.getStatusCode().is2xxSuccessful())
            throw new DomainException(ErrorCode.UNKNOWN_SERVER_ERROR);

        AppleKeyResponse[] keys = Objects.requireNonNull(keyListResponse.getBody()).keys();

        try {
            String[] tokenParts = accessToken.split("\\.");
            String header = new String(Base64.getDecoder().decode(tokenParts[0]));
            JsonNode node = objectMapper.readTree(header);
            String kid = node.get("kid").asText();

            AppleKeyResponse matchedKey = null;
            for (AppleKeyResponse key : keys){
                if(key.kid().equals(kid)) {
                    matchedKey = key;
                }
            }

            if(matchedKey == null) throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);

            Key keyData = JWK.parse(objectMapper.writeValueAsString(matchedKey)).toRSAKey().toRSAPublicKey();
            Jws<Claims> parsedClaims = Jwts.parserBuilder()
                    .setSigningKey(keyData)
                    .build()
                    .parseClaimsJws(accessToken);

            String identifier = parsedClaims.getBody().get("sub", String.class);
            return new SocialLoginResult(identifier);
        } catch(Exception ex) {
            throw new DomainException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
