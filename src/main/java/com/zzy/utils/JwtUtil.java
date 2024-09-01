package com.zzy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String secret = "waziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwazi";
    private long expire;
    private String header;

    // 生成jwt token
    public static String generateTokenByNames(String username) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7);

        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");

        return JWT.create()
                .withHeader(headerClaims)
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(secret));
    }

    // 获取jwt的信息
    public Map<String, Claim> getClaimByToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaims();
    }

    // 获取token的过期时间
    public Date getExpiration(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }

    // token是否过期
    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }

    // 验证token
    public boolean verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;
    }

}
