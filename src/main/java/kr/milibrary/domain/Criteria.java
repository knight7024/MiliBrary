package kr.milibrary.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import kr.milibrary.exception.BadRequestException;
import kr.milibrary.exception.BaseException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

@ApiIgnore
public class Criteria {
    @ApiParam(defaultValue = "1")
    protected int page;
    @ApiParam(defaultValue = "10")
    protected int limit;

    public static class SearchCriteria extends Criteria {
        @ApiParam(value = "검색어", required = true)
        private String query;
        @ApiParam(value = "author(저자) 또는 title(제목)", defaultValue = "title")
        private String target;

        public SearchCriteria(int page, int limit, String query, String target) {
            super(page, limit);
            this.query = query;
            this.target = target;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = Optional.ofNullable(target)
                    .filter(keyword -> Arrays.stream(Book.Search.values())
                            .anyMatch(search -> keyword.equalsIgnoreCase(search.name())))
                    .orElse(Book.Search.TITLE.getColumnName());
        }
    }

    public static class SortBySingleCriteria extends Criteria {
        @ApiParam(value = "year(연도) 또는 qtr(분기)", required = true)
        private String sortBy;
        @ApiParam(value = "asc(오름차순) 또는 desc(내림차순)", defaultValue = "asc")
        private String order;

        public SortBySingleCriteria(int page, int limit, String sortBy, String order) {
            super(page, limit);
            this.sortBy = sortBy;
            this.order = order;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = Book.Sort.valueOf(
                    Optional.ofNullable(sortBy)
                            .filter(keyword -> Arrays.stream(Book.Sort.values())
                                    .anyMatch(sort -> keyword.equalsIgnoreCase(sort.name())))
                            .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."))
            ).getColumnName();
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = Optional.ofNullable(order)
                    .filter(keyword -> keyword.equalsIgnoreCase("ASC") || keyword.equalsIgnoreCase("DESC"))
                    .orElse("ASC");
        }
    }

    public static class SortByMultipleCriteria extends Criteria {
        @ApiParam(value = "1개 이상의 `기준:순서`(ex. year:asc,qtr:desc)", required = true)
        private String sort;

        public SortByMultipleCriteria(int page, int limit, String sort) {
            super(page, limit);
            this.sort = sort;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            try {
                // ',' 기준으로 자른 뒤
                String[] sortPair = sort.split(",");

                // :로 자르고
                List<String> sortingKeys = new ArrayList<>();
                Set<String> keywordMap = new HashSet<>();
                for (String item : sortPair) {
                    String[] itemPair = item.split(":");
                    String columnName = itemPair[0];
                    String order = itemPair[1];

                    // column, order 검사
                    // 하나라도 잘못되면 에러 반환
                    columnName = Book.Sort.valueOf(
                            Optional.ofNullable(columnName)
                                    .filter(keyword -> Arrays.stream(Book.Sort.values())
                                            .anyMatch(sorting -> keyword.equalsIgnoreCase(sorting.name())))
                                    .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."))
                    ).getColumnName();

                    order = Optional.ofNullable(order)
                            .filter(keyword -> keyword.equalsIgnoreCase("ASC") || keyword.equalsIgnoreCase("DESC"))
                            .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."));

                    if (!keywordMap.contains(columnName)) {
                        keywordMap.add(columnName);
                    } else {
                        throw new BadRequestException("잘못된 요청 구문입니다.");
                    }

                    sortingKeys.add(String.format("%s %s", columnName, order));
                }

                this.sort = String.join(", ", sortingKeys);
            } catch (BaseException baseException) {
                throw baseException;
            } catch (Exception exception) {
                throw new BadRequestException("잘못된 요청 구문입니다.");
            }
        }
    }

    public Criteria(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = Math.min(limit, 50);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(page, 1);
    }

    @ApiModelProperty(hidden = true)
    public int getCursor() {
        return (page - 1) * limit;
    }
}