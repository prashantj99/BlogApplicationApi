package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.entities.UserResponse;
import org.prashant.blog.blogapplicationapi.payload.UserDto;

import java.util.List;

public interface UserService {
    public UserDto createUser(UserDto userDto);
    public UserDto updateUser(UserDto userDto);
    public void deleteUser(Long userId);
    public UserResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
