package org.prashant.blog.blogapplicationapi.payload;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class PostDto {
    private Long postId;
    private String postTitle;
    private String postContent;
    private String imageName;
    private Date addedDate;
    private Date lastUpdateDate;
    private CategoryDto category;
    private UserDto user;
    private List<TagDto> tags;
    private List<CommentDto> comments;
}
