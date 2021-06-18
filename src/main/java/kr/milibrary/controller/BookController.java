package kr.milibrary.controller;

import io.swagger.annotations.*;
import kr.milibrary.annotation.Auth;
import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Book;
import kr.milibrary.domain.BookList;
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
            @ApiResponse(code = 200, message = "", response = Book.class)
    })
    @GetMapping("/book/random")
    public ResponseEntity<BaseResponse> getRandomBooks(@ApiParam(value = "1~50", defaultValue = "5", example = "5") @RequestParam Integer size) {
        BaseResponse response = bookService.getRandomBooks(size);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "전체 책 불러오기", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BookList.class)
    })
    @GetMapping("/books")
    public ResponseEntity<BaseResponse> getBooks(@RequestParam Integer size) {
        BaseResponse response = bookService.getBooks(size);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }
}
