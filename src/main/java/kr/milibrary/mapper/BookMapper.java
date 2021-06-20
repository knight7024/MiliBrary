package kr.milibrary.mapper;

import kr.milibrary.domain.Book;
import kr.milibrary.domain.Criteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bookMapper")
public interface BookMapper {
    @Select("SELECT id, year, quarter, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books WHERE id = #{bookId};")
    Book getBook(@Param("bookId") int bookId);

    @Select("SELECT t1.id, t1.year, t1.quarter, t1.categoryName, t1.title, t1.description, t1.itemPage, t1.isbn, t1.pub_date, t1.authors, t1.thumbnail " +
            "FROM milibrary.books AS t1 " +
            "JOIN (" +
            "SELECT id FROM milibrary.books ORDER BY RAND()" +
            ") AS t2 " +
            "ON t1.id = t2.id LIMIT #{size};")
    List<Book> getRandomBooks(@Param("size") Integer size);

    @Select("SELECT id, year, quarter, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books ORDER BY ${criteria.sortBy} ${criteria.order};")
    List<Book> getBooksSortBySingle(@Param("criteria") Criteria.SortBySingleCriteria criteria);

    @Select("SELECT id, year, quarter, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books ORDER BY ${criteria.sort};")
    List<Book> getBooksSortByMultiple(@Param("criteria") Criteria.SortByMultipleCriteria criteria);

    @Select("SELECT id, year, quarter, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books WHERE ${criteria.target} LIKE CONCAT('%', IF(#{criteria.query} = '', null, #{criteria.query}), '%');")
    List<Book> searchBooks(@Param("criteria") Criteria.SearchCriteria criteria);
}
