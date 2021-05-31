package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.*;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "response")
public class BaseResponse {
    protected String responseMessage;
    protected BaseDomain responseResult;
    @JsonIgnore
    protected HttpStatus responseStatus;

    public BaseResponse(String responseMessage, BaseDomain responseResult, HttpStatus responseStatus) {
        this.responseMessage = responseMessage;
        this.responseResult = responseResult;
        this.responseStatus = responseStatus;
    }

    public BaseResponse(String responseMessage, HttpStatus responseStatus) {
        this.responseMessage = responseMessage;
        this.responseStatus = responseStatus;
    }

    @JsonGetter("message")
    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @JsonGetter("result")
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
