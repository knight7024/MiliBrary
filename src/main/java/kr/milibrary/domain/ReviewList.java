package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ReviewList extends BaseDomain {
    protected List<Review> reviewList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Float averageScore = null;

    public ReviewList() {
    }

    public ReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewList(List<Review> reviewList, Float averageScore) {
        this.reviewList = reviewList;
        this.averageScore = averageScore;
    }

    @JsonGetter("reviews")
    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }
}
