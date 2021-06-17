package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

public class BookList extends BaseDomain {
    protected List<Book> bookList;

    public BookList() {
    }

    public BookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @JsonGetter("books")
    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
