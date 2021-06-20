package kr.milibrary.domain;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import kr.milibrary.exception.BaseException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

@ApiIgnore
public class Criteria {
    @ApiParam(defaultValue = "1")
    protected int page = 1;
    @ApiParam(defaultValue = "10")
    protected int limit = 10;

    public static class SearchCriteria extends Criteria {
        @ApiParam(value = "검색어", required = true)
        private String query;
        @ApiParam(value = "author(저자) 또는 title(제목)", defaultValue = "title")
        private String target = Book.SearchType.TITLE.getTypeName();

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
            this.target = Book.SearchType.valueOf(
                    Optional.of(target.trim())
                            .filter(keyword -> !keyword.isEmpty() & Arrays.stream(Book.SearchType.values())
                                    .anyMatch(searchType -> keyword.equalsIgnoreCase(searchType.name())))
                            .orElse(Book.SearchType.TITLE.name())
                    .toUpperCase()
            ).getTypeName();
        }
    }

    public static class SortBySingleCriteria extends Criteria {
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
            this.sortBy = Book.SortType.valueOf(sortBy.trim().toUpperCase()).getTypeName();
//                    Optional.of(sortBy.trim())
//                            .filter(keyword -> !keyword.isEmpty() & Arrays.stream(Book.SortType.values())
//                                    .anyMatch(sortType -> keyword.equalsIgnoreCase(sortType.name())))
//                            .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."))
//                            .toUpperCase()
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

    public static class SortByMultipleCriteria extends Criteria {
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
                Set<String> keywordMap = new HashSet<>();
                for (String item : sortPair) {
                    String[] itemPair = item.split(":");
                    String columnName = itemPair[0].trim();
                    String order = itemPair[1].trim();

                    // column, order 검사
                    // 하나라도 잘못되면 에러 반환
                    columnName = Book.SortType.valueOf(columnName.toUpperCase()).getTypeName();
//                            Optional.of(columnName)
//                                    .filter(keyword -> !keyword.isEmpty() & Arrays.stream(Book.SortType.values())
//                                            .anyMatch(sorting -> keyword.equalsIgnoreCase(sorting.name())))
//                                    .orElseThrow(() -> new BadRequestException("잘못된 요청 구문입니다."))
//                                    .toUpperCase()

                    order = Optional.of(order)
                            .filter(keyword -> !keyword.isEmpty() & (keyword.equalsIgnoreCase("ASC") || keyword.equalsIgnoreCase("DESC")))
                            .orElseThrow(IllegalArgumentException::new);

                    if (!keywordMap.contains(columnName)) {
                        keywordMap.add(columnName);
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