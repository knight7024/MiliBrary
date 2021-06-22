package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseDomain {
    @ApiModelProperty(value = "유저의 나라사랑 아이디", example = "'1994070246341'", required = true)
    @NotEmpty(message = "나라사랑 아이디는 필수항목입니다.", groups = {Default.class, UserGroups.sendEmail.class})
    @Pattern(regexp = "^[0-9]{13}$", message = "올바른 나라사랑 아이디의 형식이 아닙니다.", groups = {Default.class, UserGroups.sendEmail.class})
    protected String narasarangId;
    @ApiModelProperty(value = "유저의 비밀번호", example = "1q2w3e4r!!", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "비밀번호는 필수항목입니다")
    protected String password;
    @ApiModelProperty(readOnly = true)
    protected String nickname;
    protected Boolean isRegistered;
    @ApiModelProperty(readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime registeredAt;
    @JsonIgnore
    protected LocalDateTime createdAt;

    @ApiModelProperty(hidden = true)
    protected Jwt jwt = null;

    public User() {
    }

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

    @JsonIgnore
    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
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

    @JsonGetter("tokens")
    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public void update(User user) {
        if (user.getPassword() != null)
            password = user.getPassword();
        if (user.getRegistered() != null)
            isRegistered = user.getRegistered();
    }
}
