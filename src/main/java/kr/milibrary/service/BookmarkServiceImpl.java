package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Bookmark;
import kr.milibrary.domain.BookmarkList;
import kr.milibrary.domain.User;
import kr.milibrary.exception.ConflictException;
import kr.milibrary.exception.NotFoundException;
import kr.milibrary.mapper.BookmarkMapper;
import kr.milibrary.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("bookmarkService")
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkMapper bookmarkMapper;
    private final UserMapper userMapper;

    @Autowired
    public BookmarkServiceImpl(BookmarkMapper bookmarkMapper, UserMapper userMapper) {
        this.bookmarkMapper = bookmarkMapper;
        this.userMapper = userMapper;
    }

    private Bookmark getMyBookmarkById(String narasarangId, int bookmarkId) throws NotFoundException {
        return Optional.ofNullable(bookmarkMapper.getMyBookmarkById(narasarangId, bookmarkId)).orElseThrow(() -> new NotFoundException("해당 북마크가 존재하지 않습니다."));
    }

    private Bookmark getBookmarkById(int bookmarkId) throws NotFoundException {
        return Optional.ofNullable(bookmarkMapper.getBookmarkById(bookmarkId)).orElseThrow(() -> new NotFoundException("해당 북마크가 존재하지 않습니다."));
    }

    private User getUserByNarasarangId(String narasarangId) throws NotFoundException {
        return Optional.ofNullable(userMapper.getUserByNarasarangId(narasarangId)).orElseThrow(() -> new NotFoundException("해당 나라사랑 아이디는 가입되지 않았거나 존재하지 않습니다."));
    }

    @Override
    public BaseResponse createBookmark(String narasarangId, int bookId) {
        try {
            User dbUser = getUserByNarasarangId(narasarangId);

            Bookmark bookmark = new Bookmark();
            bookmark.setBookId(bookId);
            bookmark.setNarasarangId(dbUser.getNarasarangId());
            bookmarkMapper.createBookmark(bookmark);

            return new BaseResponse(getBookmarkById(bookmark.getId()), HttpStatus.CREATED);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new ConflictException("북마크는 중복해서 생성할 수 없습니다.");
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new NotFoundException("해당 책이 존재하지 않습니다.");
        }
    }

    @Override
    public BaseResponse updateBookmark(String narasarangId, int bookmarkId, Bookmark bookmark) {
        User dbUser = getUserByNarasarangId(narasarangId);

        Bookmark dbBookmark = getMyBookmarkById(dbUser.getNarasarangId(), bookmarkId);
        dbBookmark.update(bookmark);
        bookmarkMapper.updateBookmark(bookmarkId, bookmark);

        return new BaseResponse(dbBookmark, HttpStatus.CREATED);
    }

    @Override
    public BaseResponse deleteBookmark(String narasarangId, int bookmarkId) {
        User dbUser = getUserByNarasarangId(narasarangId);

        if (bookmarkMapper.deleteBookmark(dbUser.getNarasarangId(), bookmarkId) == 0) {
            throw new NotFoundException("해당 북마크가 존재하지 않습니다.");
        }

        return new BaseResponse(HttpStatus.NO_CONTENT);
    }

    @Override
    public BaseResponse getMyBookmarks(String narasarangId) {
        return new BaseResponse(new BookmarkList(bookmarkMapper.getMyBookmarks(narasarangId)), HttpStatus.OK);
    }

    @Override
    public BaseResponse getBookmark(int bookmarkId) {
        return new BaseResponse(getBookmarkById(bookmarkId), HttpStatus.OK);
    }
}
