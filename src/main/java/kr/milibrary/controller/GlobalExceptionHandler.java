package kr.milibrary.controller;

import kr.milibrary.exception.BadRequestException;
import kr.milibrary.exception.BaseException;
import kr.milibrary.exception.UnprocessableEntityException;
import org.apache.ibatis.binding.BindingException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<? extends BaseException> BindException(BindException bindException) {
        BindingResult bindingResult = bindException.getBindingResult();
        List<String> errorMessages = new ArrayList<>();
        if (bindingResult.hasFieldErrors()) {
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            // BindingFailure??? ?????? ?????? => validator??? ?????? ????????? ????????????.
            fieldErrorList.forEach(fieldError -> {
                if (!fieldError.isBindingFailure()) {
                    errorMessages.add(fieldError.getDefaultMessage());
                }
            });
        }

        if (errorMessages.isEmpty()) {
            BadRequestException badRequestException = new BadRequestException("????????? ???????????????.");
            return new ResponseEntity<>(badRequestException, badRequestException.getErrorStatus());
        }

        UnprocessableEntityException unprocessableEntityException = new UnprocessableEntityException(errorMessages);
        return new ResponseEntity<>(unprocessableEntityException, unprocessableEntityException.getErrorStatus());
    }
}