package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ForbiddenException extends BaseException {
    public ForbiddenException(List<String> errorMessages) {
        super(HttpStatus.FORBIDDEN, errorMessages);
    }

    public ForbiddenException(String... errorMessage) {
        super(HttpStatus.FORBIDDEN, errorMessage);
    }
}
