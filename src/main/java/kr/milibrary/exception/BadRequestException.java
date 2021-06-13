package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestException extends BaseException {
    public BadRequestException(List<String> errorMessages) {
        super(HttpStatus.BAD_REQUEST, errorMessages);
    }

    public BadRequestException(String... errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
