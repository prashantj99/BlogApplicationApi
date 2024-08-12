package org.prashant.blog.blogapplicationapi.payload;


import java.util.List;

public record PostPageResponse(List<PostDTO> posts,
                               Integer pageNumber,
                               Integer pageSize,
                               Long totalRecords,
                               Integer totalPages,
                               Boolean isLastPage){
}
