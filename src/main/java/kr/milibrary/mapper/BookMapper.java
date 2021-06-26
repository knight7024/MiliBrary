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
            "SELECT id FROM milibrary.books ORDER BY RAND() LIMIT #{size}" +
            ") AS t2 " +
            "ON t1.id = t2.id;")
    List<Book> getRandomBooks(@Param("size") Integer size);

    @Select("SELECT id, year, quarter, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books ORDER BY ${criteria.sortBy} ${criteria.order} LIMIT #{criteria.limit} OFFSET #{criteria.offset};")
    List<Book> getBooksSortBySingle(@Param("criteria") Book.SortBySingleCriteria criteria);

    @Select("SELECT id, year, quarter, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books ORDER BY ${criteria.sort} LIMIT #{criteria.limit} OFFSET #{criteria.offset};")
    List<Book> getBooksSortByMultiple(@Param("criteria") Book.SortByMultipleCriteria criteria);

    @Select("SELECT id, year, quarter, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books WHERE ${criteria.target} LIKE CONCAT('%', IF(#{criteria.query} = '', null, #{criteria.query}), '%') LIMIT #{criteria.limit} OFFSET #{criteria.offset};")
    List<Book> searchBooks(@Param("criteria") Book.SearchCriteria criteria);

    @Select("SELECT COUNT(*) FROM milibrary.books;")
    long getTotalCount();
}
