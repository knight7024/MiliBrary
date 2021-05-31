package kr.milibrary.controller;

import io.swagger.annotations.ApiParam;
import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.domain.ReviewList;
import kr.milibrary.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/book/{bookId}/review")
    public ResponseEntity<BaseResponse> createReview(@PathVariable int bookId, @RequestBody Review review) {
        BaseResponse response = reviewService.createReview(bookId, review);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @GetMapping("/book/{bookId}/reviews")
    public ResponseEntity<BaseResponse> getReviews(@PathVariable int bookId) {
        BaseResponse response = reviewService.getReviews(bookId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @PatchMapping("/book/{bookId}/review/{reviewId}")
    public ResponseEntity<BaseResponse> updateReview(@PathVariable int bookId, @PathVariable int reviewId, @RequestBody Review review) {
        BaseResponse response = reviewService.updateReview(bookId, reviewId, review);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @DeleteMapping("/book/{bookId}/review/{reviewId}")
    public ResponseEntity<BaseResponse> deleteReview(@PathVariable int bookId, @PathVariable int reviewId) {
        BaseResponse response = reviewService.deleteReview(bookId, reviewId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @GetMapping("/review/random")
    public ResponseEntity<BaseResponse> getRandomReviews(@ApiParam(value = "1~100", required = true) @RequestParam Integer size) {
        BaseResponse response = reviewService.getRandomReviews(size);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }
}
