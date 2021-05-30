package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {
    protected Integer id;
    protected String narasarangId;
    protected String token;
    protected String tokenType;
    protected Boolean isUsed = false;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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

    public Token() {
    }

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

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
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

    public void update(Token token) {
        if (token.getUsed() != null)
            this.isUsed = token.getUsed();
    }

    public boolean isExpired(long hourToAdd) {
        // 발송 시간에 (hourToAdd) 시간 더한 시간이 지금 시간보다 전이면 만료
        return createdAt.plusHours(hourToAdd).isBefore(LocalDateTime.now()) | isUsed;
    }
}
