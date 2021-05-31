package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.*;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "response")
public class BaseResponse {
    protected String responseMessage;
    @JsonIgnore
    protected HttpStatus responseStatus;

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

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(HttpStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
