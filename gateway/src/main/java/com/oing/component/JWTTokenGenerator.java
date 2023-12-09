package com.oing.component;

import com.oing.config.properties.TokenProperties;
import com.oing.domain.SocialLoginProvider;
import com.oing.domain.Token;
import com.oing.domain.TokenPair;
import com.oing.domain.TokenType;
import com.oing.domain.exception.DomainException;
import com.oing.domain.exception.ErrorCode;
import com.oing.domain.exception.TokenNotValidException;
import com.oing.service.TokenGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
    private static final String PROVIDER_KEY_NAME = "provider";
    private static final String TOKEN_TYPE_KEY_NAME = "type";
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
    public TokenPair generateTemporaryTokenPair(SocialLoginProvider provider, String identifier) {
        return new TokenPair(generateTemporaryToken(provider, identifier), null);
    }

    @Override
    public Token extractTokenData(String token) {
        try {
            Jws<Claims> tokenClaim = Jwts.parserBuilder().setSigningKey(this.signKey).build()
                    .parseClaimsJws(token);

            String tokenTypeStr = (String) tokenClaim.getHeader().get(TOKEN_TYPE_KEY_NAME);
            TokenType tokenType = TokenType.fromString(tokenTypeStr);
            String userId = tokenClaim.getBody().get(USER_ID_KEY_NAME, String.class);
            String provider = tokenClaim.getBody().get(PROVIDER_KEY_NAME, String.class);
            return new Token(userId, tokenType, provider);
        } catch(Exception e){
            throw new DomainException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private Date generateAccessTokenExpiration() {
        return new Date(System.currentTimeMillis() + Long.parseLong(tokenProperties.expiration().accessToken()));
    }

    private Date generateRefreshTokenExpiration() {
        return new Date(System.currentTimeMillis() + Long.parseLong(tokenProperties.expiration().refreshToken()));
    }

    private Date generateTemporaryTokenExpiration() {
        return new Date(Long.MAX_VALUE);
    }

    private String generateTemporaryToken(SocialLoginProvider provider, String identifier) {
        return Jwts.builder()
                .setHeader(createTokenHeader(TokenType.TEMPORARY))
                .setClaims(Map.of(USER_ID_KEY_NAME, identifier, PROVIDER_KEY_NAME, provider.name()))
                .setExpiration(generateTemporaryTokenExpiration())
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateAccessToken(String userId) {
        return Jwts.builder()
                    .setHeader(createTokenHeader(TokenType.ACCESS))
                    .setClaims(Map.of(USER_ID_KEY_NAME, userId))
                    .setExpiration(generateAccessTokenExpiration())
                    .signWith(signKey, SignatureAlgorithm.HS256)
                    .compact();
    }

    private String generateRefreshToken() {
        return Jwts.builder()
                .setHeader(createTokenHeader(TokenType.REFRESH))
                .setClaims(Map.of())
                .setExpiration(generateRefreshTokenExpiration())
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> createTokenHeader(TokenType tokenType) {
        return Map.of(
                "typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis(),
                TOKEN_TYPE_KEY_NAME, tokenType.getTypeKey()
        );
    }
}
