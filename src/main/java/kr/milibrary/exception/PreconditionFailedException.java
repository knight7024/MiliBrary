package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

public class PreconditionFailedException extends BaseException {
    public PreconditionFailedException(String errorMessage) {
        super(errorMessage, HttpStatus.PRECONDITION_FAILED);
    }
}
