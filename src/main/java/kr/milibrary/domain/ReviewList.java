package kr.milibrary.domain;

import java.util.List;

public class ReviewList {
    protected List<Review> reviewList;
    protected Float averageScore;

    public ReviewList() {
    }

    public ReviewList(List<Review> reviewList, Float averageScore) {
        this.reviewList = reviewList;
        this.averageScore = averageScore;
    }

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
