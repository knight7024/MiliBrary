package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(List<String> errorMessages) {
        super(HttpStatus.UNAUTHORIZED, errorMessages);
    }

    public UnauthorizedException(String... errorMessage) {
        super(HttpStatus.UNAUTHORIZED, errorMessage);
    }
}