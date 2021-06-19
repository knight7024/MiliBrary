package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Book;
import kr.milibrary.domain.BookList;
import kr.milibrary.exception.BadRequestException;
import kr.milibrary.exception.BaseException;
import kr.milibrary.exception.NotFoundException;
import kr.milibrary.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("bookService")
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    private Book getBookById(int bookId) {
        return Optional.ofNullable(bookMapper.getBook(bookId)).orElseThrow(() -> new NotFoundException("해당 책이 존재하지 않습니다."));
    }

    @Override
    public BaseResponse getBook(int bookId) {
        return new BaseResponse(getBookById(bookId), HttpStatus.OK);
    }

    @Override
    public BaseResponse getRandomBooks(Integer size) {
        size = Optional.ofNullable(size).orElse(5);

        return new BaseResponse(new BookList(bookMapper.getRandomBooks(size > 50 ? 50 : size < 1 ? 1 : size)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getBooksSortBySingle(String sortBy, String order) {
        // SQL Injection을 방지하기 위해 정렬 키워드 검사
        sortBy = Book.Sorting.valueOf(
                Optional.ofNullable(sortBy)
                        .map(String::toUpperCase)
                        .filter(keyword -> Arrays.stream(Book.Sorting.values())
                                .anyMatch(sorting -> keyword.equals(sorting.name())))
                        .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."))
        ).getColumnName();

        order = Optional.ofNullable(order)
                .map(String::toUpperCase)
                .filter(keyword -> keyword.equals("ASC") || keyword.equals("DESC"))
                .orElse("ASC");

        return new BaseResponse(new BookList(bookMapper.getBooksSortBySingle(sortBy, order)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getBooksSortByMultiple(String sort) {
        try {
            // ',' 기준으로 자른 뒤
            String[] sortPair = sort.split(",");

            // :로 자르고
            List<String> sortingKeys = new ArrayList<>();
            Set<String> keywordMap = new HashSet<>();
            for (String item : sortPair) {
                String[] itemPair = item.split(":");
                String columnName = itemPair[0];
                String order = itemPair[1];

                // column, order 검사
                // 하나라도 잘못되면 에러 반환
                columnName = Book.Sorting.valueOf(
                        Optional.ofNullable(columnName)
                                .map(String::toUpperCase)
                                .filter(keyword -> Arrays.stream(Book.Sorting.values())
                                        .anyMatch(sorting -> keyword.equals(sorting.name())))
                                .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."))
                ).getColumnName();

                order = Optional.ofNullable(order)
                        .map(String::toUpperCase)
                        .filter(keyword -> keyword.equals("ASC") || keyword.equals("DESC"))
                        .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."));

                if (!keywordMap.contains(columnName)) {
                    keywordMap.add(columnName);
                } else {
                    throw new BadRequestException("잘못된 요청 구문입니다.");
                }

                sortingKeys.add(String.format("%s %s", columnName, order));
            }

            return new BaseResponse(new BookList(bookMapper.getBooksSortByMultiple(String.join(", ", sortingKeys))), HttpStatus.OK);
        } catch (BaseException baseException) {
            throw baseException;
        } catch (Exception exception) {
            throw new BadRequestException("잘못된 요청 구문입니다.");
        }
    }
}
