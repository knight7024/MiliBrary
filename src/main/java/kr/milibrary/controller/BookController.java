package kr.milibrary.controller;

import io.swagger.annotations.*;
import kr.milibrary.annotation.Auth;
import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Book;
import kr.milibrary.domain.BookList;
import kr.milibrary.domain.Criteria;
import kr.milibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Auth
@RequestMapping("/api")
@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ApiOperation(value = "책 상세정보 불러오기", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = Book.class),
            @ApiResponse(code = 404, message = "해당 책이 존재하지 않을 때")
    })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<BaseResponse> getBook(@PathVariable int bookId) {
        BaseResponse response = bookService.getBook(bookId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "랜덤 책 불러오기", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BookList.class)
    })
    @GetMapping("/book/random")
    public ResponseEntity<BaseResponse> getRandomBooks(@ApiParam(value = "1~10", defaultValue = "5", example = "5") @RequestParam Integer size) {
        BaseResponse response = bookService.getRandomBooks(size);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "전체 책 불러오기(한 가지 컬럼으로 정렬)", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BookList.class),
            @ApiResponse(code = 400, message = "잘못된 쿼리를 입력했을 때")
    })
    @GetMapping(value = "/books", params = "sortBy")
    public ResponseEntity<BaseResponse> getBooksSortBySingle(Criteria.SortBySingleCriteria criteria) {
        BaseResponse response = bookService.getBooksSortBySingle(criteria);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "전체 책 불러오기(여러 가지 컬럼으로 정렬)", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BookList.class),
            @ApiResponse(code = 400, message = "잘못된 쿼리를 입력했을 때")
    })
    @GetMapping(value = "/books", params = "sort")
    public ResponseEntity<BaseResponse> getBooksSortByMultiple(Criteria.SortByMultipleCriteria criteria) {
        BaseResponse response = bookService.getBooksSortByMultiple(criteria);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "책 검색하기", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BookList.class)
    })
    @GetMapping(value = "/search/book")
    public ResponseEntity<BaseResponse> searchBooks(Criteria.SearchCriteria criteria) {
        BaseResponse response = bookService.searchBooks(criteria);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }
}
