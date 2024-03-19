package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public class CategoryDto {
    private Long categoryId;

    private String categoryTitle;

    private String categoryDescription;

    private List<PostDto> posts;
}
