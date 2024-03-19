package org.prashant.blog.blogapplicationapi.payload;

import lombok.Data;
import org.prashant.blog.blogapplicationapi.entities.Role;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String about;
    private String imgUrl;
    private List<PostDto> posts;

    private List<CommentDto> comments;

    private Set<Role> roles;
}
