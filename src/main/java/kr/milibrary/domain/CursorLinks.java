package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CursorLinks {
    private String next;

    public CursorLinks() {
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
