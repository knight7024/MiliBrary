package kr.milibrary.controller;

import kr.milibrary.domain.BaseResponse;
import kr.milibrary.domain.User;
import kr.milibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/user")
@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signUp(@RequestBody User user) {
        BaseResponse response = userService.signUp(user);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @ResponseBody
    @PostMapping("/signup/resend")
    public ResponseEntity<BaseResponse> signUpResend(@RequestBody User user) {
        BaseResponse response = userService.signUpResend(user);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @GetMapping("/auth")
    public String auth(@RequestParam(value = "token") String token) {
        return userService.auth(token) ? "signup-success" : "error-page";
    }

    @ResponseBody
    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse> forgotPassword(@RequestBody User user) {
        BaseResponse response = userService.forgotPassword(user);
        return new ResponseEntity<>(response, response.getResponseStatus());
    }

    @GetMapping("/reset-password")
    public String resetPasswordAuth(@RequestParam(value = "token") String token, Model model) {
        Map<String, Object> result = userService.resetPasswordAuth(token);
        if (!((boolean) result.get("success")) || (boolean) result.get("isExpired"))
            return "error-page";

        model.addAttribute("contextURL", result.get("contextURL"));
        model.addAttribute("token", result.get("token"));
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, Object> variables) {
        return userService.resetPassword(variables) ? "reset-password-success" : "error-page";
    }
}
