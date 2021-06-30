package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Book;

public interface BookService {
    BaseResponse getBook(String narasarangId, int bookId);

    BaseResponse getRandomBooks(Integer size);

    BaseResponse getBooksSortBySingle(Book.SortBySingleCriteria sortBySingleCriteria);

    BaseResponse getBooksSortByMultiple(Book.SortByMultipleCriteria sortByMultipleCriteria);

    BaseResponse searchBooks(Book.SearchCriteria searchCriteria);
}
