package com.oing.component;

import com.oing.config.properties.TokenProperties;
import com.oing.domain.TokenPair;
import com.oing.domain.exception.TokenNotValidException;
import com.oing.service.TokenGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 6:25 PM
 */
@Component
public class JWTTokenGenerator implements TokenGenerator {
    private static final String USER_ID_KEY_NAME = "userId";
    private final TokenProperties tokenProperties;
    private final Key signKey;

    public JWTTokenGenerator(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;

        byte[] secretKeyArray = tokenProperties.secretKey().getBytes();
        this.signKey = new SecretKeySpec(secretKeyArray, SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    public TokenPair generateTokenPair(String userId) {
        String accessToken = generateAccessToken(userId);
        String refreshToken = generateRefreshToken();
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public String getUserIdFromAccessToken(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(this.signKey).build()
                    .parseClaimsJws(accessToken)
                    .getBody().get(USER_ID_KEY_NAME, String.class);
        } catch(Exception e){
            throw new TokenNotValidException();
        }
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(this.signKey).build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    private Date generateAccessTokenExpiration() {
        return new Date(System.currentTimeMillis() + tokenProperties.expiration().accessToken());
    }

    private Date generateRefreshTokenExpiration() {
        return new Date(System.currentTimeMillis() + tokenProperties.expiration().accessToken());
    }

    private String generateAccessToken(String userId) {
        return Jwts.builder()
                    .setHeader(createTokenHeader())
                    .setClaims(Map.of(USER_ID_KEY_NAME, userId))
                    .setExpiration(generateAccessTokenExpiration())
                    .signWith(signKey, SignatureAlgorithm.HS256)
                    .compact();
    }

    private String generateRefreshToken() {
        return Jwts.builder()
                .setHeader(createTokenHeader())
                .setClaims(Map.of())
                .setExpiration(generateRefreshTokenExpiration())
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> createTokenHeader() {
        return Map.of(
                "typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis());
    }
}
