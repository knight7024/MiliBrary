package kr.milibrary.exception;

import com.fasterxml.jackson.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties({"stackTrace", "suppressed", "cause", "message", "localizedMessage"})
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "error")
public class BaseException extends RuntimeException {
    protected List<String> errorMessages = new ArrayList<>();
    @JsonIgnore
    protected HttpStatus errorStatus;

    BaseException(HttpStatus errorStatus, List<String> errorMessages) {
        this.errorStatus = errorStatus;
        this.errorMessages = errorMessages;
    }

    BaseException(HttpStatus errorStatus, String... errorMessage) {
        this.errorStatus = errorStatus;
        errorMessages.addAll(Arrays.asList(errorMessage));
    }

    @JsonGetter("messages")
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public HttpStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(HttpStatus errorStatus) {
        this.errorStatus = errorStatus;
    }
}
