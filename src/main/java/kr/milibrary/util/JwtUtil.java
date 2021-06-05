package kr.milibrary.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.milibrary.exception.BadRequestException;
import kr.milibrary.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

// ToDo: JWT 인증
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private Algorithm algorithmHS;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> stringStringValueOperations;

    @PostConstruct
    public void setAlgorithmHS() {
        algorithmHS = Algorithm.HMAC256(jwtSecret);
    }

    private enum JwtType {
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

    public String createAccessToken(boolean isAdmin) {
        String accessToken = null;
        try {
            accessToken = JWT.create()
                    .withSubject(JwtType.ACCESS_TOKEN.getJwtType())
                    .withExpiresAt(Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant()))
                    .withClaim("isAdmin", isAdmin)
                    .sign(algorithmHS);

            stringStringValueOperations.setIfAbsent("user:1", accessToken, Duration.ofHours(2));
        }
        catch (JWTCreationException jwtCreationException) {

        }
        return accessToken;
    }

    public String createRefreshToken(String narasarangId) {
        String refreshToken = null;
        try {
            refreshToken = JWT.create()
                    .withSubject(JwtType.REFRESH_TOKEN.getJwtType())
                    .withAudience(narasarangId)
                    .withExpiresAt(Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()))
                    .sign(algorithmHS);

            stringStringValueOperations.setIfAbsent("user:1", refreshToken, Duration.ofDays(30));
        }
        catch (JWTCreationException jwtCreationException) {

        }
        return refreshToken;
    }

    public void validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);

        }
        catch (JWTDecodeException jwtDecodeException) {
            throw new BadRequestException("올바르지 않은 토큰입니다.");
        }
        catch (TokenExpiredException tokenExpiredException) {
            throw new UnauthorizedException("만료된 토큰입니다. 토큰을 갱신하고 다시 시도해주세요.");
        }
    }
}
