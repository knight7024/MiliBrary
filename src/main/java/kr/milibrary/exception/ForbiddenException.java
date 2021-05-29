package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String errorMessage) {
        super(errorMessage, HttpStatus.FORBIDDEN);
    }
}
