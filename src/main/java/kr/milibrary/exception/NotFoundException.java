package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class NotFoundException extends BaseException {
    public NotFoundException(List<String> errorMessages) {
        super(HttpStatus.NOT_FOUND, errorMessages);
    }

    public NotFoundException(String... errorMessage) {
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}
