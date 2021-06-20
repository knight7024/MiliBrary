package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Book;
import kr.milibrary.domain.BookList;
import kr.milibrary.domain.Criteria;
import kr.milibrary.exception.NotFoundException;
import kr.milibrary.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("bookService")
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    private Book getBookById(int bookId) throws NotFoundException {
        return Optional.ofNullable(bookMapper.getBook(bookId)).orElseThrow(() -> new NotFoundException("해당 책이 존재하지 않습니다."));
    }

    @Override
    public BaseResponse getBook(int bookId) {
        return new BaseResponse(getBookById(bookId), HttpStatus.OK);
    }

    @Override
    public BaseResponse getRandomBooks(Integer size) {
        size = Optional.ofNullable(size).orElse(5);

        return new BaseResponse(new BookList(bookMapper.getRandomBooks(size > 10 ? 10 : size < 1 ? 1 : size)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getBooksSortBySingle(Criteria.SortBySingleCriteria criteria) {
        return new BaseResponse(new BookList(bookMapper.getBooksSortBySingle(criteria)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getBooksSortByMultiple(Criteria.SortByMultipleCriteria criteria) {
        return new BaseResponse(new BookList(bookMapper.getBooksSortByMultiple(criteria)), HttpStatus.OK);
    }

    @Override
    public BaseResponse searchBooks(Criteria.SearchCriteria criteria) {
        return new BaseResponse(new BookList(bookMapper.searchBooks(criteria)), HttpStatus.OK);
    }
}