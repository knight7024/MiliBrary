package kr.milibrary.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
public class Criteria {
    @ApiParam(value = "1 이상", defaultValue = "1")
    protected int page = 1;
    @ApiParam(value = "10 이상 50 이하", defaultValue = "10")
    protected int limit = 10;

    public Criteria() {
    }

    public Criteria(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = Math.min(Math.max(limit, 10), 50);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(page, 1);
    }

    @ApiModelProperty(hidden = true)
    public int getOffset() {
        return (page - 1) * limit;
    }
}