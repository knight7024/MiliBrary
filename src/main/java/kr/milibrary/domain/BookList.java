package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookList extends BaseDomain {
    protected Integer totalPage = null;
    protected List<Book> bookList;

    public BookList() {
    }

    public BookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public BookList(Integer totalPage, List<Book> bookList) {
        this.totalPage = totalPage;
        this.bookList = bookList;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
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
