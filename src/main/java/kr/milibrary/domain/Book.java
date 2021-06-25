package kr.milibrary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import kr.milibrary.exception.BaseException;

import java.time.LocalDate;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book extends BaseDomain {
    protected Integer id;
    @ApiModelProperty(value = "해당 진중문고가 제공된 연도")
    protected Integer year;
    @ApiModelProperty(value = "해당 진중문고가 제공된 분기(1~4)")
    protected Integer quarter;
    protected String categoryName;
    protected String title;
    protected String description;
    protected Integer itemPage;
//    protected String content;
    protected String isbn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    protected LocalDate pubDate;
    protected String authors;
    @JsonIgnore
    protected String publisher;
    protected String thumbnail;

    protected Float averageScore = null;

    public enum SortType {
        YEAR("year"),
        QTR("quarter"),
        AVG("averageScore")
        ;

        private final String typeName;

        SortType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    public enum SearchType {
        AUTHOR("authors"),
        TITLE("title")
        ;

        private final String typeName;

        SearchType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    public static class SearchCriteria extends Criteria.OffsetCriteria {
        @ApiParam(value = "검색어", required = true)
        private String query;
        @ApiParam(value = "author(저자) 또는 title(제목)", defaultValue = "title")
        private String target = SearchType.TITLE.getTypeName();

        public SearchCriteria() {
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query.trim();
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = SearchType.valueOf(
                    Optional.of(target.trim())
                            .filter(keyword -> !keyword.isEmpty() & Arrays.stream(SearchType.values())
                                    .anyMatch(searchType -> keyword.equalsIgnoreCase(searchType.name())))
                            .orElse(SearchType.TITLE.name())
                            .toUpperCase()
            ).getTypeName();
        }
    }

    public static class SortBySingleCriteria extends Criteria.OffsetCriteria {
        @ApiParam(value = "year(연도) 또는 qtr(분기)", required = true)
        private String sortBy;
        @ApiParam(value = "asc(오름차순) 또는 desc(내림차순)", defaultValue = "asc")
        private String order = "ASC";

        public SortBySingleCriteria() {
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = SortType.valueOf(sortBy.trim().toUpperCase()).getTypeName();
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = Optional.of(order.trim())
                    .filter(keyword -> !keyword.isEmpty() & (keyword.equalsIgnoreCase("ASC") || keyword.equalsIgnoreCase("DESC")))
                    .orElse("ASC");
        }
    }

    public static class SortByMultipleCriteria extends Criteria.OffsetCriteria {
        @ApiParam(value = "1개 이상의 `기준:순서`(ex. year:asc,qtr:desc)", required = true)
        private String sort;

        public SortByMultipleCriteria() {
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            try {
                // ',' 기준으로 자른 뒤
                String[] sortPair = sort.trim().split(",");
                if (sortPair.length == 0)
                    throw new IllegalArgumentException();

                // :로 자르고
                List<String> sortingKeys = new ArrayList<>();
                Set<String> columnNameSet = new HashSet<>();
                for (String item : sortPair) {
                    String[] itemPair = item.split(":");
                    String columnName = itemPair[0].trim();
                    String order = itemPair[1].trim();

                    // column, order 검사
                    // 하나라도 잘못되면 에러 반환
                    columnName = SortType.valueOf(columnName.toUpperCase()).getTypeName();

                    order = Optional.of(order)
                            .filter(keyword -> !keyword.isEmpty() & (keyword.equalsIgnoreCase("ASC") || keyword.equalsIgnoreCase("DESC")))
                            .orElseThrow(IllegalArgumentException::new);

                    if (!columnNameSet.contains(columnName)) {
                        columnNameSet.add(columnName);
                    } else {
                        throw new IllegalArgumentException();
                    }

                    sortingKeys.add(String.format("%s %s", columnName, order));
                }

                this.sort = String.join(", ", sortingKeys);
            } catch (BaseException baseException) {
                throw baseException;
            } catch (Exception exception) {
                throw new IllegalArgumentException();
            }
        }
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getItemPage() {
        return itemPage;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }
}
