package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;

import java.util.List;

public interface ReviewService {
    BaseResponse createReview(int bookId, Review review);
    List<Review> getReviews(int id);
    Review getReview(int bookId, int reviewId);
    BaseResponse updateReview(int bookId, int reviewId);
    BaseResponse deleteReview(int bookId, int reviewId);
}
