package org.prashant.blog.blogapplicationapi.payload;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CategoryDto {
    private Long categoryId;
    private String categoryTitle;
    private String categoryDescription;
    @JsonIgnore
    private List<PostDto> posts;
}
