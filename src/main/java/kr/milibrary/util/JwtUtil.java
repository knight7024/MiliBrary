package kr.milibrary.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

// ToDo: 인터셉터에서 사용할 JWT 인증 작업 추가
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private Algorithm algorithmHS;

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

    public String createAccessToken(boolean isAdmin) {
        return JWT.create()
                    .withSubject(JwtType.ACCESS_TOKEN.getJwtType())
                    .withExpiresAt(Date.from(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant()))
                    .withClaim("isAdmin", isAdmin)
                    .sign(algorithmHS);
    }

    public String createRefreshToken(String narasarangId) {
        return JWT.create()
                    .withSubject(JwtType.REFRESH_TOKEN.getJwtType())
                    .withAudience(narasarangId)
                    .withExpiresAt(Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant()))
                    .sign(algorithmHS);
    }

    public boolean validateRefreshToken(String token) {
        return true;
    }

    public boolean isExpired(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
        } catch (TokenExpiredException tokenExpiredException) {
            return true;
        }

        return false;
    }
}
