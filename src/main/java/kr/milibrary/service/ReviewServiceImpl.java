package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Review;
import kr.milibrary.domain.ReviewList;
import kr.milibrary.domain.User;
import kr.milibrary.exception.ConflictException;
import kr.milibrary.exception.NotFoundException;
import kr.milibrary.mapper.ReviewMapper;
import kr.milibrary.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper, UserMapper userMapper) {
        this.reviewMapper = reviewMapper;
        this.userMapper = userMapper;
    }

    private Review getReviewById(int bookId, int reviewId) throws NotFoundException {
        return Optional.ofNullable(reviewMapper.getReviewById(bookId, reviewId)).orElseThrow(() -> new NotFoundException("해당 리뷰가 존재하지 않습니다."));
    }

    private User getUserByNarasarangId(String narasarangId) throws NotFoundException {
        return Optional.ofNullable(userMapper.getUserByNarasarangId(narasarangId)).orElseThrow(() -> new NotFoundException("해당 나라사랑 아이디는 가입되지 않았거나 존재하지 않습니다."));
    }

    @Override
    public BaseResponse createReview(String narasarangId, int bookId, Review review) throws ConflictException {
        try {
            User dbUser = getUserByNarasarangId(narasarangId);

            review.setNarasarangId(dbUser.getNarasarangId());
            reviewMapper.createReview(bookId, review);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new ConflictException("리뷰는 중복해서 작성할 수 없습니다.");
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new NotFoundException("해당 책이 존재하지 않습니다.");
        }

        return new BaseResponse(getReviewById(bookId, review.getId()), HttpStatus.CREATED);
    }

    @Override
    public BaseResponse getReviews(int bookId, Review.CursorCriteria criteria) throws NotFoundException {
        List<Review> reviewList = new ArrayList<>();
        // 정렬 기준이 평점이라면
        if (Review.SortType.SCORE.getTypeName().equals(criteria.getSortBy())) {
            if ("ASC".equalsIgnoreCase(criteria.getOrder())) {
                reviewList = reviewMapper.getReviewsByScoreAsc(bookId, criteria);
            } else if ("DESC".equalsIgnoreCase(criteria.getOrder())) {
                reviewList = reviewMapper.getReviewsByScoreDesc(bookId, criteria);
            }
        }
        // 정렬 기준이 작성일자라면
        else if (Review.SortType.DATE.getTypeName().equals(criteria.getSortBy())) {
            if ("ASC".equalsIgnoreCase(criteria.getOrder())) {
                reviewList = reviewMapper.getReviewsByDateAsc(bookId, criteria);
            } else if ("DESC".equalsIgnoreCase(criteria.getOrder())) {
                reviewList = reviewMapper.getReviewsByDateDesc(bookId, criteria);
            }
        }

        return new BaseResponse(new ReviewList(reviewList), HttpStatus.OK);
    }

    @Override
    public BaseResponse updateReview(String narasarangId, int bookId, int reviewId, Review review) {
        User dbUser = getUserByNarasarangId(narasarangId);

        Review dbReview = getReviewById(bookId, reviewId);
        dbReview.update(review);
        reviewMapper.updateReview(bookId, reviewId, dbReview);

        return new BaseResponse(dbReview, HttpStatus.CREATED);
    }

    @Override
    public BaseResponse deleteReview(String narasarangId, int bookId, int reviewId) throws NotFoundException {
        User dbUser = getUserByNarasarangId(narasarangId);

        if (reviewMapper.deleteReview(bookId, reviewId) == 0) {
            throw new NotFoundException("해당 리뷰가 존재하지 않습니다.");
        }

        return new BaseResponse(HttpStatus.NO_CONTENT);
    }

    @Override
    public BaseResponse getRandomReviews(Integer size) {
        size = Optional.ofNullable(size).orElse(5);

        return new BaseResponse(new ReviewList(reviewMapper.getRandomReviews(size > 10 ? 10 : size < 1 ? 1 : size)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getMyReview(String narasarangId, int bookId) {
        User dbUser = getUserByNarasarangId(narasarangId);

        return new BaseResponse(reviewMapper.getMyReview(dbUser.getNarasarangId(), bookId), HttpStatus.OK);
    }

    @Override
    public BaseResponse getMyReviews(String narasarangId, Review.CursorCriteria criteria) {
        User dbUser = getUserByNarasarangId(narasarangId);

        return new BaseResponse(new ReviewList(reviewMapper.getMyReviews(dbUser.getNarasarangId(), criteria)), HttpStatus.OK);
    }
}
