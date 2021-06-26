package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookList extends BaseDomain {
    private Long totalPage = null;
    private List<Book> bookList;

    public BookList() {
    }

    public BookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public BookList(Long totalPage, List<Book> bookList) {
        this.totalPage = totalPage;
        this.bookList = bookList;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    @JsonGetter("books")
    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
