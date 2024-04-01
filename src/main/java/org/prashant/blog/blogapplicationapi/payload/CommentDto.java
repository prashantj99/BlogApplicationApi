package org.prashant.blog.blogapplicationapi.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {
    private Long commentId;

    private String commentText;

    private PostDto post;

    private UserDto user;

    private Date commentDate;

    private Date lastUpdated;
}
