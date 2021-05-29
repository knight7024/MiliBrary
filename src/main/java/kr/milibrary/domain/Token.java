package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {
    protected Integer id;
    protected String narasarangId;
    protected String token;
    protected String tokenType;
    protected LocalDateTime createdAt;
    protected TokenType tokenTypeEnum;

    public enum TokenType {
        REGISTRATION("registration", "signup"),
        FORGOT_PASSWORD("forgot_password", "forgot-password"),
        RESEND_REGISTRATION("resend_registration", "signup")
        ;

        private String tokenType;
        private String templateName;

        TokenType(String tokenType, String templateName) {
            this.tokenType = tokenType;
            this.templateName = templateName;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }
    }
    
    Token() {}

    public Token(String narasarangId, String token, TokenType tokenTypeEnum) {
        this.narasarangId = narasarangId;
        this.token = token;
        this.tokenTypeEnum = tokenTypeEnum;
        this.tokenType = tokenTypeEnum.getTokenType();
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNarasarangId() {
        return narasarangId;
    }

    public void setNarasarangId(String narasarangId) {
        this.narasarangId = narasarangId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
        this.tokenTypeEnum = TokenType.valueOf(tokenType.toUpperCase());
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TokenType getTokenTypeEnum() {
        return tokenTypeEnum;
    }

    public void setTokenTypeEnum(TokenType tokenTypeEnum) {
        this.tokenTypeEnum = tokenTypeEnum;
        this.tokenType = tokenTypeEnum.getTokenType();
    }

    public boolean isExpired(long hourToAdd) {
        // 발송 시간에 (hourToAdd) 시간 더한 시간이 지금 시간보다 전이면 만료
        return this.createdAt.plusHours(hourToAdd).isBefore(LocalDateTime.now());
    }
}
