package kr.milibrary.controller;

import io.swagger.annotations.*;
import kr.milibrary.annotation.Auth;
import kr.milibrary.annotation.AuthIgnore;
import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.domain.ReviewList;
import kr.milibrary.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Auth
@RequestMapping("/api")
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @ApiOperation(value = "리뷰 작성", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "리뷰 작성에 성공했을 때"),
            @ApiResponse(code = 409, message = "리뷰를 중복해서 작성했을 때")
    })
    @PostMapping("/book/{bookId}/review")
    public ResponseEntity<BaseResponse> createReview(@PathVariable int bookId, @RequestBody Review review) {
        BaseResponse response = reviewService.createReview(bookId, review);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @AuthIgnore
    @ApiOperation(value = "특정 책에 대한 전체 리뷰 불러오기")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = ReviewList.class),
            @ApiResponse(code = 404, message = "입력한 책 id가 존재하지 않을 때")
    })
    @GetMapping("/book/{bookId}/reviews")
    public ResponseEntity<BaseResponse> getReviews(@PathVariable int bookId) {
        BaseResponse response = reviewService.getReviews(bookId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "리뷰 수정", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "리뷰 수정에 성공했을 때"),
            @ApiResponse(code = 404, message = "입력한 리뷰 id가 존재하지 않을 때")
    })
    @PatchMapping("/book/{bookId}/review/{reviewId}")
    public ResponseEntity<BaseResponse> updateReview(@PathVariable int bookId, @PathVariable int reviewId, @RequestBody Review review) {
        BaseResponse response = reviewService.updateReview(bookId, reviewId, review);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "리뷰 삭제", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "리뷰 삭제에 성공했을 때 아무런 반환값도 주지 않는다."),
            @ApiResponse(code = 404, message = "입력한 리뷰 id가 존재하지 않을 때")
    })
    @DeleteMapping("/book/{bookId}/review/{reviewId}")
    public ResponseEntity<BaseResponse> deleteReview(@PathVariable int bookId, @PathVariable int reviewId) {
        BaseResponse response = reviewService.deleteReview(bookId, reviewId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @AuthIgnore
    @ApiOperation(value = "오늘의 랜덤 리뷰")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = ReviewList.class)
    })
    @GetMapping("/review/random")
    public ResponseEntity<BaseResponse> getRandomReviews(@ApiParam(value = "1~100", defaultValue = "5", example = "5") @RequestParam Integer size) {
        BaseResponse response = reviewService.getRandomReviews(size);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }
}
