package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import org.springframework.stereotype.Service;

@Service("bookService")
public interface BookService {
    BaseResponse getBook(int bookId);
    BaseResponse getBooks(Integer size);
}
