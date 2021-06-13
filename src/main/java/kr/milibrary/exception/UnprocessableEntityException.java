package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UnprocessableEntityException extends BaseException {
    public UnprocessableEntityException(List<String> errorMessages) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorMessages);
    }

    public UnprocessableEntityException(String... errorMessage) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
    }
}
