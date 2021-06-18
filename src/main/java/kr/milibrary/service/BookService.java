package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;

public interface BookService {
    BaseResponse getBook(int bookId);
    BaseResponse getRandomBooks(Integer size);
    BaseResponse getBooks(Integer size);
}
