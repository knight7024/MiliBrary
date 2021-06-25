package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewList extends BaseDomain {
    protected List<Review> reviewList;

    public ReviewList() {
    }

    public ReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @JsonGetter("reviews")
    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }
}
