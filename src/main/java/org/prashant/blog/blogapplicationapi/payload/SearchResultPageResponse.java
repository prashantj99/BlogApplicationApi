package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record SearchResultPageResponse(
        List<?> results,
        Integer pageNumber,
        Integer pageSize,
        Long totalRecords,
        Integer totalPages,
        Boolean isLastPage
) {
}
