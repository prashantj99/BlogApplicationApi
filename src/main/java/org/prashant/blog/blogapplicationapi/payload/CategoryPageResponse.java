package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record CategoryPageResponse(List<CategoryDTO> categories, int number, int size, long totalElements, int totalPages, boolean last){
}
