package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.*;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    protected BaseDomain responseResult;
    @JsonIgnore
    protected HttpStatus responseStatus;

    public BaseResponse(BaseDomain responseResult, HttpStatus responseStatus) {
        this.responseResult = responseResult;
        this.responseStatus = responseStatus;
    }

    public BaseResponse(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    @JsonUnwrapped
    public BaseDomain getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(BaseDomain responseResult) {
        this.responseResult = responseResult;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
