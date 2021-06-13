package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ConflictException extends BaseException {
    public ConflictException(List<String> errorMessages) {
        super(HttpStatus.CONFLICT, errorMessages);
    }

    public ConflictException(String... errorMessage) {
        super(HttpStatus.CONFLICT, errorMessage);
    }
}
