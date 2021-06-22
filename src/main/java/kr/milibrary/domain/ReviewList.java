package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewList extends BaseDomain {
    protected Integer totalPage = null;
    protected List<Review> reviewList;

    public ReviewList() {
    }

    public ReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewList(Integer totalPage, List<Review> reviewList) {
        this.totalPage = totalPage;
        this.reviewList = reviewList;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @JsonGetter("reviews")
    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }
}
