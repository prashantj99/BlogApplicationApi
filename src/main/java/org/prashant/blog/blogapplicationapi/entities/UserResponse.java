package org.prashant.blog.blogapplicationapi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.prashant.blog.blogapplicationapi.payload.UserDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private List<UserDto> content;
    private int pageNumber;
    private int pageSize;
    private int records;
    private int totalPages;
    private boolean isLastPage;
}
