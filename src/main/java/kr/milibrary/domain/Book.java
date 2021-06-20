package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book extends BaseDomain {
    protected Integer id;
    @ApiModelProperty(value = "해당 진중문고가 제공된 연도")
    protected Integer year;
    @ApiModelProperty(value = "해당 진중문고가 제공된 분기(1~4)")
    protected Integer quarter;
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

    public enum SortType {
        YEAR("year"),
        QTR("quarter");

        private final String typeName;

        SortType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    public enum SearchType {
        AUTHOR("authors"),
        TITLE("title");

        private final String typeName;

        SearchType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getQuarter() {
        return quarter;
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
