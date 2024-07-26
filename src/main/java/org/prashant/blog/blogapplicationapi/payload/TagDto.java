package org.prashant.blog.blogapplicationapi.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    private Long tagId;
    private String tagName;
    @JsonIgnore
    private List<PostDto> posts;
}
