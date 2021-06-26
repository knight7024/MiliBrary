package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Jwt;
import kr.milibrary.domain.Token;
import kr.milibrary.domain.User;

import java.sql.SQLException;
import java.util.Map;

public interface UserService {
    BaseResponse signUp(User user);
    BaseResponse signUpResend(User user);
    BaseResponse signIn(User user);
    BaseResponse signOut(String narasarangId);
    BaseResponse refresh(Jwt.RefreshToken refreshToken);
    boolean auth(String token);
    BaseResponse forgotPassword(User user);
    Map<String, Object> resetPasswordAuth(String token);
    boolean resetPassword(Map<String, Object> variables);
    BaseResponse withdraw(User user);
}
