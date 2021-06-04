package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.domain.ReviewList;
import kr.milibrary.exception.ConflictException;
import kr.milibrary.exception.NotFoundException;
import kr.milibrary.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    private Review getReviewById(int bookId, int reviewId) throws NotFoundException {
        Optional<Review> reviewOptional = Optional.ofNullable(reviewMapper.getReviewById(bookId, reviewId));
        return reviewOptional.orElseThrow(() -> new NotFoundException("해당 리뷰가 존재하지 않습니다."));
    }

    @Override
    public BaseResponse createReview(int bookId, Review review) throws ConflictException {
        try {
            reviewMapper.createReview(bookId, review);
        } catch (DuplicateKeyException e) {
            throw new ConflictException("리뷰는 중복해서 작성할 수 없습니다.");
        }
        return new BaseResponse("리뷰 작성에 성공했습니다.", getReviewById(bookId, review.getId()), HttpStatus.CREATED);
    }

    @Override
    public BaseResponse getReviews(int bookId) throws NotFoundException {
        Optional<ReviewList> reviewListOptional = Optional.of(new ReviewList(reviewMapper.getReviews(bookId), reviewMapper.getAverageScore(bookId)));
        reviewListOptional.map(ReviewList::getAverageScore).orElseThrow(() -> new NotFoundException("해당 책이 존재하지 않습니다."));
        return new BaseResponse(null, reviewListOptional.get(), HttpStatus.OK);
    }

    @Override
    public BaseResponse updateReview(int bookId, int reviewId, Review review) {
        Review dbReview = getReviewById(bookId, reviewId);
        dbReview.update(review);
        reviewMapper.updateReview(bookId, reviewId, dbReview);
        return new BaseResponse("리뷰 수정에 성공했습니다.", dbReview, HttpStatus.CREATED);
    }

    @Override
    public BaseResponse deleteReview(int bookId, int reviewId) throws NotFoundException {
        if (reviewMapper.deleteReview(bookId, reviewId) == 0)
            throw new NotFoundException("해당 리뷰가 존재하지 않습니다.");
        return new BaseResponse("리뷰 삭제에 성공했습니다.", HttpStatus.NO_CONTENT);
    }

    @Override
    public BaseResponse getRandomReviews(Integer size) {
        size = Optional.ofNullable(size).orElse(5);
        return new BaseResponse(null, new ReviewList(reviewMapper.getRandomReviews(size > 100 ? 100 : size < 1 ? 1 : size)), HttpStatus.OK);
    }
}
