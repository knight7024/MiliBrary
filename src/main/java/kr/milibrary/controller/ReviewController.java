package kr.milibrary.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/books/review")
@RestController
public class ReviewController {
    @ApiOperation(value = "테스트")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> testAPI()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", "Test End");
        return result;
    }
}
