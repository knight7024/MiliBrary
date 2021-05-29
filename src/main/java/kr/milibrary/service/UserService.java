package kr.milibrary.service;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Token;
import kr.milibrary.domain.User;

import java.sql.SQLException;
import java.util.Map;

public interface UserService {
    BaseResponse signUp(User user) throws SQLException;
    BaseResponse signUpResend(User user) throws SQLException;
    BaseResponse signIn(User user);
    boolean auth(String token);
    BaseResponse forgotPassword(User user) throws SQLException;
    Map<String, Object> resetPasswordAuth(String token);
    boolean resetPassword(Map<String, Object> variables);
}
