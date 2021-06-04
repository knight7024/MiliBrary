package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.domain.ReviewList;

import java.util.List;

public interface ReviewService {
    BaseResponse createReview(int bookId, Review review);
    BaseResponse getReviews(int bookId);
    BaseResponse updateReview(int bookId, int reviewId, Review review);
    BaseResponse deleteReview(int bookId, int reviewId);
    BaseResponse getRandomReviews(Integer size);
}
