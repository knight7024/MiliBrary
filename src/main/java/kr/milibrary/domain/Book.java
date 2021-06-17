package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Book extends BaseDomain {
    protected Integer id;
    protected String categoryName;
    protected String title;
    protected String description;
    protected Integer itemPage;
//    protected String content;
    protected String isbn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    protected LocalDate pubDate;
    protected String authors;
    protected String publisher;
    protected String thumbnail;

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getItemPage() {
        return itemPage;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
