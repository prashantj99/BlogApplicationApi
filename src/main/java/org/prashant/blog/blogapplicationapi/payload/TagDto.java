package org.prashant.blog.blogapplicationapi.payload;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    private Long tagId;
    private String tagName;
    private List<PostDto> posts;
}
