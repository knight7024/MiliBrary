package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class PreconditionFailedException extends BaseException {
    public PreconditionFailedException(List<String> errorMessages) {
        super(HttpStatus.PRECONDITION_FAILED, errorMessages);
    }

    public PreconditionFailedException(String... errorMessage) {
        super(HttpStatus.PRECONDITION_FAILED, errorMessage);
    }
}
