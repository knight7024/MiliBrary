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

    @Select("SELECT * FROM milibrary.reviews WHERE book_id = #{bookId} ORDER BY created_at DESC;")
    List<Review> getReviews(@Param("bookId") int bookId);

    @Select("SELECT ROUND(AVG(score), 1) AS averageScore FROM milibrary.reviews WHERE book_id = #{bookId} GROUP BY book_id;")
    Float getAverageScore(@Param("bookId") int bookId);

    @Select("SELECT * FROM milibrary.reviews WHERE id = #{reviewId} AND book_id = #{bookId};")
    Review getReviewById(@Param("bookId") int bookId, @Param("reviewId") int reviewId);

    @Update("UPDATE milibrary.reviews SET score = #{review.score}, comment = #{review.comment} WHERE id = #{reviewId} AND book_id = #{bookId};")
    void updateReview(@Param("bookId") int bookId, @Param("reviewId") int reviewId, @Param("review") Review review);

    @Delete("DELETE FROM milibrary.reviews WHERE id = #{reviewId} AND book_id = #{bookId}")
    int deleteReview(@Param("bookId") int bookId, @Param("reviewId") int reviewId);
}