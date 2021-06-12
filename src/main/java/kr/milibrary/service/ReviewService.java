package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.domain.ReviewList;

import java.util.List;

public interface ReviewService {
    BaseResponse createReview(String narasarangId, int bookId, Review review);
    BaseResponse getReviews(int bookId);
    BaseResponse updateReview(String narasarangId, int bookId, int reviewId, Review review);
    BaseResponse deleteReview(String narasarangId, int bookId, int reviewId);
    BaseResponse getRandomReviews(Integer size);
    BaseResponse getMyReview(String narasarangId, int bookId);
    BaseResponse getMyReviews(String narasarangId);
}
