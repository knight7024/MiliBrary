package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.mapper.ReviewMapper;
import kr.milibrary.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper, UserMapper userMapper) {
        this.reviewMapper = reviewMapper;
        this.userMapper = userMapper;
    }

    @Override
    public BaseResponse createReview(int bookId, Review review) {
        return null;
    }

    @Override
    public List<Review> getReviews(int id) {
        return null;
    }

    @Override
    public Review getReview(int bookId, int reviewId) {
        return null;
    }

    @Override
    public BaseResponse updateReview(int bookId, int reviewId) {
        return null;
    }

    @Override
    public BaseResponse deleteReview(int bookId, int reviewId) {
        return null;
    }
}
