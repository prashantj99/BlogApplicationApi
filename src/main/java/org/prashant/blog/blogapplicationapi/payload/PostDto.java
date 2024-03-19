package org.prashant.blog.blogapplicationapi.payload;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDto {
    private Long postId;
    private String postTitle;
    private String postContent;
    private String imageUrl;
    private Date addedDate;
    private Date lastUpdateDate;
    private CategoryDto category;
    private UserDto user;
    private List<TagDto> tags;
    private List<CommentDto> comment;

}
