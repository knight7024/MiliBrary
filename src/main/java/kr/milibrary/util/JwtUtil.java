package kr.milibrary.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private Algorithm algorithmHS;

    private final String ISSUER = "MiliBrary";

    @PostConstruct
    private void setAlgorithmHS() {
        algorithmHS = Algorithm.HMAC256(jwtSecret);
    }

    public enum JwtType {
        ACCESS_TOKEN("accessToken"),
        REFRESH_TOKEN("refreshToken");

        private final String jwtType;

        JwtType(String jwtType) {
            this.jwtType = jwtType;
        }

        public String getJwtType() {
            return jwtType;
        }
    }

    public String createAccessToken(String narasarangId, boolean isAdmin) {
        return JWT.create()
                .withSubject(JwtType.ACCESS_TOKEN.getJwtType())
                .withAudience(narasarangId)
                .withExpiresAt(Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("isAdmin", isAdmin)
                .withIssuer(ISSUER)
                .sign(algorithmHS);
    }

    public String createRefreshToken(String narasarangId) {
        return JWT.create()
                .withSubject(JwtType.REFRESH_TOKEN.getJwtType())
                .withAudience(narasarangId)
                .withExpiresAt(Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer(ISSUER)
                .sign(algorithmHS);
    }

    // 1. 토큰의 만료 여부
    // 2. 토큰의 발행자
    // 3. 토큰 발행 대상자 존재 여부
    // 4. 토큰의 변조 여부
    public boolean isValid(String jwt, JwtType jwtType) {
        try {
            Optional<DecodedJWT> decodedJWTOptional = Optional.ofNullable(jwt)
                    .filter(t -> !isExpired(t))
                    .map(JWT::decode);
            if (decodedJWTOptional.isPresent()) {
                DecodedJWT decodeJWT = decodedJWTOptional.get();
                return ISSUER.equals(decodeJWT.getIssuer()) & jwtType.getJwtType().equals(decodeJWT.getSubject()) & !decodeJWT.getAudience().isEmpty();
            }
        } catch (JWTVerificationException jwtVerificationException) {
            return false;
        }

        return true;
    }

    public boolean forAdmin(String jwt) {
        DecodedJWT decodedJWT = Optional.ofNullable(jwt)
                .filter(t -> isValid(t, JwtType.ACCESS_TOKEN))
                .map(JWT::decode)
                .orElseThrow(() -> new JWTDecodeException(""));

        return decodedJWT.getClaim("isAdmin").asBoolean();
    }

    public boolean isExpired(String jwt) {
        try {
            JWT.decode(jwt);
        } catch (TokenExpiredException tokenExpiredException) {
            return true;
        }

        return false;
    }

    public DecodedJWT getDecodedJWT(String jwt) {
        try {
            return JWT.decode(jwt);
        } catch (JWTVerificationException jwtVerificationException) {
            return null;
        }
    }
}
