package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Criteria;

public interface BookService {
    BaseResponse getBook(int bookId);
    BaseResponse getRandomBooks(Integer size);
    BaseResponse getBooksSortBySingle(Criteria.SortBySingleCriteria sortBySingleCriteria);
    BaseResponse getBooksSortByMultiple(Criteria.SortByMultipleCriteria sortByMultipleCriteria);
    BaseResponse searchBooks(Criteria.SearchCriteria searchCriteria);
}
