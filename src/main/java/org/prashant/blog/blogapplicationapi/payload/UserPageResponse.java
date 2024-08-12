package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record UserPageResponse(List<UserDTO> content, Integer pageNumber,
                               Integer pageSize,
                               Long totalRecords,
                               Integer totalPages,
                               Boolean isLastPage) {
}
