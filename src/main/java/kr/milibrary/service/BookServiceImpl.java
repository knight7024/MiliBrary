package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Book;
import kr.milibrary.domain.BookList;
import kr.milibrary.domain.Criteria;
import kr.milibrary.exception.NotFoundException;
import kr.milibrary.mapper.BookMapper;
import kr.milibrary.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("bookService")
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final ReviewMapper reviewMapper;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper, ReviewMapper reviewMapper) {
        this.bookMapper = bookMapper;
        this.reviewMapper = reviewMapper;
    }

    private Book getBookById(int bookId) throws NotFoundException {
        return Optional.ofNullable(bookMapper.getBook(bookId)).orElseThrow(() -> new NotFoundException("해당 책이 존재하지 않습니다."));
    }

    @Override
    public BaseResponse getBook(int bookId) {
        Book dbBook = getBookById(bookId);
        dbBook.setAverageScore(reviewMapper.getAverageScore(bookId));

        return new BaseResponse(dbBook, HttpStatus.OK);
    }

    @Override
    public BaseResponse getRandomBooks(Integer size) {
        size = Optional.ofNullable(size).orElse(5);

        return new BaseResponse(new BookList(bookMapper.getRandomBooks(size > 10 ? 10 : size < 1 ? 1 : size)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getBooksSortBySingle(Book.SortBySingleCriteria criteria) {
        return new BaseResponse(new BookList(calculateTotalPage(bookMapper.getTotalCount(), criteria.getLimit()), bookMapper.getBooksSortBySingle(criteria)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getBooksSortByMultiple(Book.SortByMultipleCriteria criteria) {
        return new BaseResponse(new BookList(calculateTotalPage(bookMapper.getTotalCount(), criteria.getLimit()), bookMapper.getBooksSortByMultiple(criteria)), HttpStatus.OK);
    }

    @Override
    public BaseResponse searchBooks(Book.SearchCriteria criteria) {
        return new BaseResponse(new BookList(calculateTotalPage(bookMapper.getTotalCountBySearch(criteria), criteria.getLimit()), bookMapper.searchBooks(criteria)), HttpStatus.OK);
    }

    private long calculateTotalPage(long totalCount, long limit) {
        return (long) Math.ceil((double) totalCount / limit);
    }
}
