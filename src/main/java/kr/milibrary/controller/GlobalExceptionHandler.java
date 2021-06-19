package kr.milibrary.controller;

import kr.milibrary.exception.BaseException;
import kr.milibrary.exception.UnprocessableEntityException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<BaseException> BaseException(BaseException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<UnprocessableEntityException> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors())
            errorMessages.add(error.getDefaultMessage());

        UnprocessableEntityException unprocessableEntityException = new UnprocessableEntityException(errorMessages);
        return new ResponseEntity<>(unprocessableEntityException, unprocessableEntityException.getErrorStatus());
    }
}