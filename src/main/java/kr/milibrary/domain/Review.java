package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Review extends BaseDomain {
    @ApiModelProperty(readOnly = true)
    protected Integer id;
    @ApiModelProperty(readOnly = true)
    protected Integer bookId;
    @ApiModelProperty(value = "유저의 나라사랑 아이디", example = "'1994070246341'", readOnly = true)
    protected String narasarangId;
    @ApiModelProperty(value = "유저가 매긴 평점(소수점 첫째 자리까지)", example = "2.5", required = true)
    protected Float score;
    @ApiModelProperty(value = "유저가 남긴 리뷰", example = "재밌습니다.", required = true)
    protected String comment;
    @ApiModelProperty(readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime createdAt;
    @ApiModelProperty(readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime updatedAt;

    @ApiModelProperty(hidden = true)
    protected String nickname;

    public Review() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getNarasarangId() {
        return narasarangId;
    }

    public void setNarasarangId(String narasarangId) {
        this.narasarangId = narasarangId;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNickname() {
        return nickname;
    }

    public void update(Review review) {
        if (review.getScore() != null)
            score = review.getScore();
        if (review.getComment() != null)
            comment = review.getComment();
    }
}
