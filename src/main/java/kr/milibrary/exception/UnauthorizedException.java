package kr.milibrary.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String errorMessage) {
        super(errorMessage, HttpStatus.UNAUTHORIZED);
    }

//    public String toString() {
//        return "UnauthorizeException{" +
//                "errorMessage=" + "" +
//                '}';
//    }
}