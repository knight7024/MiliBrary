package kr.milibrary.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    protected String narasarangId;
    protected String password;
    protected String nickname;
    protected Boolean isRegistered;
    protected Boolean isDeleted;
    protected LocalDateTime registeredAt;
    protected LocalDateTime createdAt;

    public String getNarasarangId() {
        return narasarangId;
    }

    public void setNarasarangId(String narasarangId) {
        this.narasarangId = narasarangId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void update(User user) {
        if (user.getPassword() != null)
            this.password = user.getPassword();
        if (user.getRegistered() != null)
            this.isRegistered = user.getRegistered();
        if (user.getDeleted() != null)
            this.isDeleted = user.getDeleted();
    }
}
