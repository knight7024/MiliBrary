package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import kr.milibrary.util.JwtUtil;

import java.beans.ConstructorProperties;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Jwt extends BaseDomain {
    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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

        @ApiModelProperty(readOnly = true)
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

        @ConstructorProperties({"token"})
        public RefreshToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @ApiModelProperty(readOnly = true)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
