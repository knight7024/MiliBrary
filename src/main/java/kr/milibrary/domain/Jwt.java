package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import kr.milibrary.util.JwtUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Jwt extends BaseDomain {
    @ApiModelProperty(readOnly = true)
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    public static class AccessToken {
        private String token;
        private long expiresIn = JwtUtil.JwtType.ACCESS_TOKEN.getExpiresIn();

        public AccessToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

    public static class RefreshToken {
        private String token;
        private long expiresIn = JwtUtil.JwtType.REFRESH_TOKEN.getExpiresIn();

        public RefreshToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

    public Jwt(AccessToken accessToken, RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }
}
