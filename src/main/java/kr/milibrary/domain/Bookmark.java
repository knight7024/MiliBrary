package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bookmark extends BaseDomain {
    @ApiModelProperty(readOnly = true)
    private Integer id;
    @ApiModelProperty(readOnly = true)
    private Integer bookId;
    @ApiModelProperty(value = "유저의 나라사랑 아이디", example = "'1994070246341'", readOnly = true)
    protected String narasarangId;
    @ApiModelProperty(value = "한 줄 평 내용", example = "인상 깊어서 전역 후에도 읽어보고 싶다.", required = true)
    private String content;
    @ApiModelProperty(readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @ApiModelProperty(hidden = true)
    private Book book = null;

    public Bookmark() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void update(Bookmark bookmark) {
        if (bookmark.getContent() != null) {
            this.content = bookmark.getContent();
        }
    }
}
