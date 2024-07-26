package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record CategoryPageResponse(List<CategoryDT> categories, int number, int size, long totalElements, int totalPages, boolean last){
}
