package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record CategoryPageResponse(List<CategoryDTO> categories,
                                   Integer pageNumber,
                                   Integer pageSize,
                                   Long totalRecords,
                                   Integer totalPages,
                                   Boolean isLastPage){
}
