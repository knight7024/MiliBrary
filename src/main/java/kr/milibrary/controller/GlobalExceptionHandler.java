package kr.milibrary.controller;

import kr.milibrary.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
}