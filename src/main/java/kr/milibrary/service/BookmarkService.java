package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Bookmark;

public interface BookmarkService {
    BaseResponse createBookmark(String narasarangId, int bookId);

    BaseResponse updateBookmark(String narasarangId, int bookmarkId, Bookmark bookmark);

    BaseResponse deleteBookmark(String narasarangId, int bookmarkId);

    BaseResponse getMyBookmarks(String narasarangId);

    BaseResponse getBookmark(int bookmarkId);
}
