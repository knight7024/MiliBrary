package kr.milibrary.mapper;

import kr.milibrary.domain.Bookmark;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("bookmarkMapper")
public interface BookmarkMapper {
    Bookmark getBookmarkById(@Param("bookmarkId") int bookmarkId);

    Bookmark getMyBookmarkById(@Param("narasarangId") String narasarangId, @Param("bookmarkId") int bookmarkId);

    void createBookmark(@Param("bookmark") Bookmark bookmark) throws DataAccessException;

    int deleteBookmark(@Param("narasarangId") String narasarangId, @Param("bookmarkId") int bookmarkId);

    void updateBookmark(@Param("bookmarkId") int bookmarkId, @Param("bookmark") Bookmark bookmark);

    List<Bookmark> getMyBookmarks(@Param("narasarangId") String narasarangId);
}
