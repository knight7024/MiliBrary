package kr.milibrary.mapper;

import kr.milibrary.domain.Criteria;
import kr.milibrary.domain.Review;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("reviewMapper")
public interface ReviewMapper {
    @Insert("INSERT INTO milibrary.reviews (book_id, narasarang_id, score, comment) " +
            "VALUES (#{bookId}, #{review.narasarangId}, #{review.score}, #{review.comment});")
    @Options(useGeneratedKeys = true, keyProperty = "review.id")
    void createReview(@Param("bookId") int bookId, @Param("review") Review review) throws DataAccessException;

    @Select("SELECT t1.id, t1.book_id, t1.narasarang_id, t2.nickname, t1.score, t1.comment, t1.created_at, t1.updated_at " +
            "FROM milibrary.reviews AS t1 " +
            "JOIN (" +
            "SELECT narasarang_id, nickname FROM milibrary.users" +
            ") AS t2 " +
            "ON t1.narasarang_id = t2.narasarang_id AND t1.book_id = #{bookId} " +
            "ORDER BY t1.${criteria.sortBy} ${criteria.order} LIMIT #{criteria.limit} OFFSET #{criteria.offset};")
    List<Review> getReviews(@Param("bookId") int bookId, @Param("criteria") Review.SortBySingleCriteria criteria);

    @Select("SELECT ROUND(AVG(score), 1) AS averageScore FROM milibrary.reviews WHERE book_id = #{bookId} GROUP BY book_id;")
    Float getAverageScore(@Param("bookId") int bookId);

    @Select("SELECT t1.id, t1.book_id, t1.narasarang_id, t2.nickname, t1.score, t1.comment, t1.created_at, t1.updated_at " +
            "FROM milibrary.reviews AS t1 " +
            "JOIN (" +
            "SELECT narasarang_id, nickname FROM milibrary.users" +
            ") AS t2 " +
            "ON id = #{reviewId} AND t1.narasarang_id = t2.narasarang_id AND book_id = #{bookId};")
    Review getReviewById(@Param("bookId") int bookId, @Param("reviewId") int reviewId);

    @Update("UPDATE milibrary.reviews SET score = #{review.score}, comment = #{review.comment} WHERE id = #{reviewId} AND book_id = #{bookId};")
    void updateReview(@Param("bookId") int bookId, @Param("reviewId") int reviewId, @Param("review") Review review);

    @Delete("DELETE FROM milibrary.reviews WHERE id = #{reviewId} AND book_id = #{bookId}")
    int deleteReview(@Param("bookId") int bookId, @Param("reviewId") int reviewId);

    @Select("SELECT t1.id, t1.book_id, t1.narasarang_id, t3.nickname, t1.score, t1.comment, t1.created_at, t1.updated_at " +
            "FROM (" +
            "milibrary.reviews AS t1 " +
            "JOIN (" +
            "SELECT id FROM milibrary.reviews ORDER BY RAND() LIMIT #{size}" +
            ") AS t2 " +
            "ON t1.id = t2.id" +
            ") " +
            "JOIN (" +
            "SELECT narasarang_id, nickname FROM milibrary.users" +
            ") AS t3 " +
            "ON t1.narasarang_id = t3.narasarang_id " +
            "ORDER BY t1.created_at DESC;")
    List<Review> getRandomReviews(@Param("size") Integer size);

    @Select("SELECT t1.id, t1.book_id, t1.narasarang_id, t2.nickname, t1.score, t1.comment, t1.created_at, t1.updated_at " +
            "FROM milibrary.reviews AS t1 " +
            "JOIN (" +
            "SELECT narasarang_id, nickname FROM milibrary.users WHERE narasarang_id = #{narasarangId}" +
            ") AS t2 " +
            "ON t1.narasarang_id = t2.narasarang_id AND book_id = #{bookId};")
    Review getMyReview(@Param("narasarangId") String narasarangId, @Param("bookId") int bookId);

    @Select("SELECT t1.id, t1.book_id, t1.narasarang_id, t2.nickname, t1.score, t1.comment, t1.created_at, t1.updated_at " +
            "FROM milibrary.reviews AS t1 " +
            "JOIN (" +
            "SELECT narasarang_id, nickname FROM milibrary.users WHERE narasarang_id = #{narasarangId}" +
            ") AS t2 " +
            "ON t1.narasarang_id = t2.narasarang_id ORDER BY t1.${criteria.sortBy} ${criteria.order} LIMIT #{criteria.limit} OFFSET #{criteria.offset};")
    List<Review> getMyReviews(@Param("narasarangId") String narasarangId, @Param("criteria") Review.SortBySingleCriteria criteria);

    @Select("SELECT COUNT(*) FROM milibrary.reviews;")
    int getTotalCount();

    @Select("SELECT COUNT(*) FROM milibrary.reviews WHERE narasarang_id = #{narasarangId};")
    int getTotalCountByNarasarangId(@Param("narasarangId") String narasarangId);
}