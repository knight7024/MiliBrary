package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Review extends BaseDomain {
    @ApiModelProperty(readOnly = true)
    protected Integer id;
    @ApiModelProperty(readOnly = true)
    protected Integer bookId;
    @ApiModelProperty(value = "유저의 나라사랑 아이디", example = "'1994070246341'", readOnly = true)
    protected String narasarangId;
    @ApiModelProperty(hidden = true)
    protected String nickname;
    @ApiModelProperty(value = "유저가 매긴 평점(0부터 5까지 0.5 단위)", example = "2.5", required = true)
    @NotNull(message = "평점은 필수항목입니다.")
    protected Double score;
    @ApiModelProperty(value = "유저가 남긴 리뷰", example = "재밌습니다.", required = true)
    @NotNull(message = "리뷰는 필수항목입니다.")
    protected String comment;
    @ApiModelProperty(readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    protected LocalDateTime createdAt;
    @JsonIgnore
    protected LocalDateTime updatedAt;

    public enum SortType {
        DATE("created_at"),
        SCORE("score")
        ;

        private final String typeName;

        SortType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    public static class CursorCriteria extends Criteria {
        private String cursor;
        @ApiModelProperty(hidden = true)
        private Integer lastId = null;
        @ApiModelProperty(hidden = true)
        private LocalDateTime date = null;
        @ApiModelProperty(hidden = true)
        private Double score = null;

        public CursorCriteria() {
        }

        public String getCursor() {
            return cursor;
        }

        public void setCursor(String cursor) {
            // ToDo: lastId, date, score 추출
            this.cursor = cursor;
        }

        public Integer getLastId() {
            return lastId;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public Double getScore() {
            return score;
        }
    }

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

    public Double getScore() {
        return Math.ceil(score * 2) / 2;
    }

    public void setScore(Double score) {
        this.score = Math.max(Math.min(score, 5), 0);
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
