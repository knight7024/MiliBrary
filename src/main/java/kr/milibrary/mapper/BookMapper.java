package kr.milibrary.mapper;

import kr.milibrary.domain.Book;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper {
    @Select("SELECT id, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books WHERE id = #{bookId};")
    Book getBook(@Param("bookId") int bookId);

    @Select("SELECT id, categoryName, title, description, itemPage, isbn, pub_date, authors, thumbnail FROM milibrary.books LIMIT #{size};")
    List<Book> getBooks(@Param("size") Integer size);
}
