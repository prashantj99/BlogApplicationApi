package org.prashant.blog.blogapplicationapi.payload;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.prashant.blog.blogapplicationapi.entities.Role;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String name;
    private String userEmail;
    private String userPassword;
    private String about;
    private String imgUrl;
    @JsonIgnore
    private List<PostDto> posts;
    private List<CommentDto> comments;
    private Set<Role> roles;
}
