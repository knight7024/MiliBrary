package kr.milibrary.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;

@ApiIgnore
public abstract class Criteria {
    @ApiParam(value = "10 이상 50 이하", defaultValue = "10")
    protected long limit = 10;

    public static class OffsetCriteria extends Criteria {
        @Max(value = 10000000, message = "page는 너무 클 수 없습니다.")
        @ApiParam(value = "1 이상", defaultValue = "1")
        private long page = 1;

        public OffsetCriteria() {
        }

        public long getPage() {
            return page;
        }

        public void setPage(long page) {
            this.page = Math.max(page, 1L);
        }

        @ApiModelProperty(hidden = true)
        public long getOffset() {
            return (page - 1) * limit;
        }
    }

    public Criteria() {
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = Math.min(Math.max(limit, 10L), 50L);
    }
}