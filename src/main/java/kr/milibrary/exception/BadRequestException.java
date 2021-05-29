package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(String errorMessage) {
        super(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
