package kr.milibrary.mapper;

import kr.milibrary.domain.Bookmark;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bookmarkMapper")
public interface BookmarkMapper {
    @Select("SELECT * " +
            "FROM milibrary.bookmarks AS t1 " +
            "JOIN (" +
            "SELECT * FROM milibrary.books" +
            ") AS t2 " +
            "ON t1.id = #{bookmarkId} AND t1.book_id = t2.id;")
    Bookmark getBookmarkById(@Param("bookmarkId") int bookmarkId);

    @Select("SELECT * " +
            "FROM milibrary.bookmarks AS t1 " +
            "JOIN (" +
            "SELECT * FROM milibrary.books" +
            ") AS t2 " +
            "ON id = #{bookmarkId} AND t1.book_id = t2.id AND narasarang_id = #{narasarangId}")
    Bookmark getMyBookmarkById(@Param("narasarangId") String narasarangId, @Param("bookmarkId") int bookmarkId);

    @Insert("INSERT INTO milibrary.bookmarks (book_id, narasarang_id) " +
            "VALUES (#{bookmark.bookId}, #{bookmark.narasarangId});")
    @Options(useGeneratedKeys = true, keyProperty = "bookmark.id")
    void createBookmark(@Param("bookmark") Bookmark bookmark) throws DataAccessException;

    @Delete("DELETE FROM milibrary.bookmarks WHERE id = #{bookmarkId} AND narasarang_id = #{narasarangId}")
    int deleteBookmark(@Param("narasarangId") String narasarangId, @Param("bookmarkId") int bookmarkId);

    @Update("UPDATE milibrary.bookmarks SET content = #{bookmark.content} WHERE id = #{bookmarkId};")
    void updateBookmark(@Param("bookmarkId") int bookmarkId, @Param("bookmark") Bookmark bookmark);

    @Select("SELECT * " +
            "FROM milibrary.bookmarks AS t1 " +
            "JOIN (" +
            "SELECT * FROM milibrary.books" +
            ") AS t2 " +
            "ON t1.book_id = t2.id AND narasarang_id = #{narasarangId};")
    List<Bookmark> getMyBookmarks(@Param("narasarangId") String narasarangId);
}
