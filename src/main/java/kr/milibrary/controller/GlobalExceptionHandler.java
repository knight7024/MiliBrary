package kr.milibrary.controller;

import kr.milibrary.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = UnauthorizedException.class)
    public @ResponseBody
    ResponseEntity<UnauthorizedException> UnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public @ResponseBody
    ResponseEntity<ForbiddenException> ForbiddenException(ForbiddenException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public @ResponseBody
    ResponseEntity<NotFoundException> NotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = PreconditionFailedException.class)
    public @ResponseBody
    ResponseEntity<PreconditionFailedException> PreconditionFailedException(PreconditionFailedException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = ConflictException.class)
    public @ResponseBody
    ResponseEntity<ConflictException> ConflictException(ConflictException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = BadRequestException.class)
    public @ResponseBody
    ResponseEntity<BadRequestException> BadRequestException(BadRequestException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = UnprocessableEntityException.class)
    public @ResponseBody
    ResponseEntity<UnprocessableEntityException> UnprocessableEntityException(UnprocessableEntityException e) {
        return new ResponseEntity<>(e, e.getErrorStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public @ResponseBody
    ResponseEntity<UnprocessableEntityException> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors())
            errorMessages.add(error.getDefaultMessage());

        UnprocessableEntityException unprocessableEntityException = new UnprocessableEntityException(errorMessages);
        return new ResponseEntity<>(unprocessableEntityException, unprocessableEntityException.getErrorStatus());
    }
}