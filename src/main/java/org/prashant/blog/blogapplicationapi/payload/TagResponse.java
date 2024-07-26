package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record TagResponse(
        List<TagDT> tags,
        Integer pageNumber,
        Integer pageSize,
        Long totalRecords,
        Integer totalPages,
        Boolean isLastPage
) {
}
