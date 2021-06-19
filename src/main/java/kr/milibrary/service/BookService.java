package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;

public interface BookService {
    BaseResponse getBook(int bookId);
    BaseResponse getRandomBooks(Integer size);
    BaseResponse getBooksSortBySingle(String sortBy, String order);
    BaseResponse getBooksSortByMultiple(String sort);
}
