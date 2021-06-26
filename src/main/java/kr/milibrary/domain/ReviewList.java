package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewList extends BaseDomain {
    private CursorLinks links;
    private List<Review> reviewList;

    public ReviewList() {
    }

    public ReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewList(CursorLinks links, List<Review> reviewList) {
        this.links = links;
        this.reviewList = reviewList;
    }

    public CursorLinks getLinks() {
        return links;
    }

    public void setLinks(CursorLinks links) {
        this.links = links;
    }

    @JsonGetter("reviews")
    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }
}
