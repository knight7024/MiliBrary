package kr.milibrary.controller;

import io.swagger.annotations.*;
import kr.milibrary.annotation.Auth;
import kr.milibrary.annotation.JwtSession;
import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Bookmark;
import kr.milibrary.domain.BookmarkList;
import kr.milibrary.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Auth
@RequestMapping("/api")
@RestController
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @ApiOperation(value = "북마크 생성", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "북마크 생성에 성공했을 때", response = Bookmark.class),
            @ApiResponse(code = 404, message = "해당 책이 존재하지 않을 때"),
            @ApiResponse(code = 409, message = "북마크를 중복해서 작성했을 때")
    })
    @PostMapping("/book/{bookId}/bookmark")
    public ResponseEntity<BaseResponse> createBookmark(@JwtSession @ApiParam(hidden = true) String narasarangId, @PathVariable int bookId) {
        BaseResponse response = bookmarkService.createBookmark(narasarangId, bookId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "나의 전체 북마크 불러오기", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BookmarkList.class)
    })
    @GetMapping("/bookmarks/my")
    public ResponseEntity<BaseResponse> getMyBookmarks(@JwtSession @ApiParam(hidden = true) String narasarangId) {
        BaseResponse response = bookmarkService.getMyBookmarks(narasarangId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "북마크 수정", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "북마크 수정에 성공했을 때", response = Bookmark.class),
            @ApiResponse(code = 404, message = "해당 북마크가 존재하지 않을 때")
    })
    @PatchMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<BaseResponse> updateBookmark(@JwtSession @ApiParam(hidden = true) String narasarangId, @PathVariable int bookmarkId, @RequestBody Bookmark bookmark) {
        BaseResponse response = bookmarkService.updateBookmark(narasarangId, bookmarkId, bookmark);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "북마크 삭제", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "북마크 삭제에 성공했을 때 아무런 반환값도 주지 않는다."),
            @ApiResponse(code = 404, message = "해당 북마크가 존재하지 않을 때")
    })
    @DeleteMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<BaseResponse> deleteBookmark(@JwtSession @ApiParam(hidden = true) String narasarangId, @PathVariable int bookmarkId) {
        BaseResponse response = bookmarkService.deleteBookmark(narasarangId, bookmarkId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "특정 북마크 불러오기", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = Bookmark.class),
            @ApiResponse(code = 404, message = "해당 북마크가 존재하지 않을 때")
    })
    @GetMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<BaseResponse> getBookmark(@PathVariable int bookmarkId) {
        BaseResponse response = bookmarkService.getBookmark(bookmarkId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }
}
