package org.prashant.blog.blogapplicationapi.payload;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIgnore
    private List<CommentDto> comments;
    @JsonManagedReference
    private Set<Role> roles;
    @JsonIgnore
    public String getUserPassword(){
        return this.userPassword;
    }
}
