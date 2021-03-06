package kr.milibrary.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        ACCESS_TOKEN("accessToken", TimeUnit.MINUTES.toSeconds(5)),
        REFRESH_TOKEN("refreshToken", TimeUnit.DAYS.toSeconds(30))
        ;

        private final String jwtType;
        private final long expiresIn;

        JwtType(String jwtType, long expiresIn) {
            this.jwtType = jwtType;
            this.expiresIn = expiresIn;
        }

        public String getJwtType() {
            return jwtType;
        }

        public long getExpiresIn() {
            return expiresIn;
        }
    }

    public String createAccessToken(String narasarangId, boolean isAdmin) {
        return JWT.create()
                .withSubject(JwtType.ACCESS_TOKEN.getJwtType())
                .withAudience(narasarangId)
                .withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(JwtType.ACCESS_TOKEN.getExpiresIn()).atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("isAdmin", isAdmin)
                .withIssuer(ISSUER)
                .sign(algorithmHS);
    }

    public String createRefreshToken(String narasarangId) {
        return JWT.create()
                .withSubject(JwtType.REFRESH_TOKEN.getJwtType())
                .withAudience(narasarangId)
                .withExpiresAt(Date.from(LocalDateTime.now().plusSeconds(JwtType.REFRESH_TOKEN.getExpiresIn()).atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuer(ISSUER)
                .sign(algorithmHS);
    }

    // 1. ????????? ?????? ??????
    // 2. ????????? ?????????
    // 3. ?????? ?????? ????????? ?????? ??????
    // 4. ????????? ?????? ??????
    public boolean isValid(String jwt, JwtType jwtType) {
        try {
            JWTVerifier jwtVerifier = JWT.require(algorithmHS)
                    .withIssuer(ISSUER)
                    .withSubject(jwtType.getJwtType())
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            return !decodedJWT.getAudience().isEmpty();
        } catch (JWTVerificationException jwtVerificationException) {
            return false;
        }
    }

    public boolean forAdmin(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(algorithmHS)
                    .withIssuer(ISSUER)
                    .withSubject(JwtType.ACCESS_TOKEN.getJwtType())
                    .withClaim("isAdmin", true)
                    .build();
            jwtVerifier.verify(jwt);
        } catch (JWTVerificationException jwtVerificationException) {
            return false;
        }

        return true;
    }

    public boolean isExpired(String jwt) {
        try {
            JWT.require(algorithmHS).build().verify(jwt);
        } catch (TokenExpiredException tokenExpiredException) {
            return true;
        }

        return false;
    }

    public DecodedJWT getDecodedJWT(String jwt) {
        try {
            return JWT.decode(jwt);
        } catch (JWTDecodeException jwtDecodeException) {
            return null;
        }
    }
}
