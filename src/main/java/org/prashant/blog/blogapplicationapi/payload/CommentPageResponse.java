package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record CommentPageResponse(List<CommentDTO> comments,
                                  Integer pageNumber,
                                  Integer pageSize,
                                  Long totalRecords,
                                  Integer totalPages,
                                  Boolean isLastPage
) {
}
