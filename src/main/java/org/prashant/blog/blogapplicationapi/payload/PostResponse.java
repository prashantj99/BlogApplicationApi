package org.prashant.blog.blogapplicationapi.payload;


import java.util.List;

public record PostResponse(List<PostDto> content,
                           Integer pageNumber,
                           Integer pageSize,
                           Long totalRecords,
                           Integer totalPages,
                           Boolean isLastPage){
}
