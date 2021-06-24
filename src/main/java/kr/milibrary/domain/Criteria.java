package kr.milibrary.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
public abstract class Criteria {
    @ApiParam(value = "10 이상 50 이하", defaultValue = "10")
    protected int limit = 10;

    public static class OffsetCriteria extends Criteria {
        @ApiParam(value = "1 이상", defaultValue = "1")
        private int page = 1;

        public OffsetCriteria() {
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

    public Criteria() {
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = Math.min(Math.max(limit, 10), 50);
    }
}