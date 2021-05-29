package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String errorMessage) {
        super(errorMessage, HttpStatus.CONFLICT);
    }
}
