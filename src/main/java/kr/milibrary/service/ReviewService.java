package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.domain.ReviewList;

public interface ReviewService {
    BaseResponse createReview(int bookId, Review review);
    ReviewList getReviews(int bookId);
    BaseResponse updateReview(int bookId, int reviewId, Review review);
    BaseResponse deleteReview(int bookId, int reviewId);
}
