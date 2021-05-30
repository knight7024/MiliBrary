package kr.milibrary.mapper;

import kr.milibrary.domain.Review;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewMapper {
    @Insert("INSERT INTO milibrary.reviews (book_id, narasarang_id, score, comment) " +
            "VALUES (#{bookId}, #{review.narasarangId}, #{review.score}, #{review.comment});")
    void createReview(@Param("bookId") int bookId, @Param("review") Review review) throws DataAccessException;

    @Select("SELECT * FROM milibrary.reviews WHERE book_id = #{bookId} AND is_deleted = 0;")
    List<Review> getReviews(@Param("bookId") int bookId);

    @Select("SELECT ROUND(AVG(score), 1) AS averageScore FROM milibrary.reviews WHERE book_id = #{bookId} AND is_deleted = 0 GROUP BY book_id;")
    Float getAverageScore(@Param("bookId") int bookId);

    @Select("SELECT * FROM milibrary.reviews WHERE id = #{reviewId} AND book_id = #{bookId} AND is_deleted = 0;")
    Review getReviewById(@Param("bookId") int bookId, @Param("reviewId") int reviewId);

    @Update("UPDATE milibrary.reviews SET score = #{review.score}, comment = #{review.comment}, is_deleted = #{review.deleted} WHERE id = #{reviewId} AND book_id = #{bookId};")
    void updateReview(@Param("bookId") int bookId, @Param("reviewId") int reviewId, @Param("review") Review review);
}