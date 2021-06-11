package kr.milibrary.controller;

import io.swagger.annotations.*;
import kr.milibrary.annotation.Auth;
import kr.milibrary.annotation.JwtSession;
import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.Jwt;
import kr.milibrary.domain.User;
import kr.milibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RequestMapping("/api/user")
@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "로그인")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인에 성공했을 때"),
            @ApiResponse(code = 400, message = "잘못된 아이디를 전송했을 때"),
            @ApiResponse(code = 401, message = "비밀번호가 틀렸거나 본인인증이 완료되지 않았을 때"),
            @ApiResponse(code = 404, message = "일치하는 아이디가 없을 때")
    })
    @ResponseBody
    @PostMapping("/signin")
    public ResponseEntity<BaseResponse> signIn(@RequestBody User user) {
        BaseResponse response = userService.signIn(user);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @Auth
    @ApiOperation(value = "로그아웃", authorizations = {@Authorization(value = "Authorization")})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "로그아웃에 성공했을 때"),
            @ApiResponse(code = 400, message = "잘못된 아이디를 전송했을 때"),
            @ApiResponse(code = 404, message = "일치하는 아이디가 없을 때")
    })
    @ResponseBody
    @PostMapping("/signout")
    public ResponseEntity<BaseResponse> signOut(@JwtSession @ApiParam(hidden = true) String narasarangId) {
        BaseResponse response = userService.signOut(narasarangId);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "Access Token 갱신")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Access Token 갱신에 성공했을 때"),
            @ApiResponse(code = 401, message = "Refresh Token이 유효하지 않을 때"),
    })
    @ResponseBody
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refresh(@RequestBody Jwt.RefreshToken refreshToken) {
        BaseResponse response = userService.refresh(refreshToken);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원가입에 성공했을 때"),
            @ApiResponse(code = 409, message = "이미 회원가입을 시도한 회원일 때")
    })
    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signUp(@RequestBody User user) {
        BaseResponse response = userService.signUp(user);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiOperation(value = "회원가입 인증 메일 재전송")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "인증 메일 재전송에 성공했을 때"),
            @ApiResponse(code = 400, message = "잘못된 아이디를 전송했을 때"),
            @ApiResponse(code = 404, message = "일치하는 아이디가 없을 때"),
            @ApiResponse(code = 409, message = "이미 회원가입을 시도한 회원일 때")
    })
    @ResponseBody
    @PostMapping("/signup/resend-email")
    public ResponseEntity<BaseResponse> signUpResend(@ApiParam(value = "password 속성은 필요하지 않음", required = true) @RequestBody User user) {
        BaseResponse response = userService.signUpResend(user);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiIgnore
    @GetMapping("/auth")
    public String auth(@RequestParam(value = "token") String token) {
        return userService.auth(token) ? "signup-success" : "error-page";
    }

    @ApiOperation(value = "비밀번호 재설정")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "잘못된 아이디를 전송했을 때"),
            @ApiResponse(code = 404, message = "일치하는 아이디가 없을 때"),
            @ApiResponse(code = 409, message = "회원가입을 대기 중인 회원일 때")
    })
    @ResponseBody
    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse> forgotPassword(@ApiParam(value = "password 속성은 필요하지 않음", required = true) @RequestBody User user) {
        BaseResponse response = userService.forgotPassword(user);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ApiIgnore
    @GetMapping("/reset-password")
    public String resetPasswordAuth(@RequestParam(value = "token") String token, Model model) {
        Map<String, Object> result = userService.resetPasswordAuth(token);
        if (!((boolean) result.get("success")) || (boolean) result.get("isExpired"))
            return "error-page";

        model.addAttribute("contextURL", result.get("contextURL"));
        model.addAttribute("token", result.get("token"));
        return "reset-password";
    }

    @ApiIgnore
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, Object> variables) {
        return userService.resetPassword(variables) ? "reset-password-success" : "error-page";
    }
}
