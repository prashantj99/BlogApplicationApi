package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.UserPageResponse;
import org.prashant.blog.blogapplicationapi.payload.UserDto;

public interface UserService {
    public UserDto createUser(UserDto userDto);
    public UserDto updateUser(UserDto userDto);
    public void deleteUser(Long userId);
    public UserPageResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
