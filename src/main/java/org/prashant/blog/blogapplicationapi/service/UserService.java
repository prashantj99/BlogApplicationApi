package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.UpdateUserRequest;
import org.prashant.blog.blogapplicationapi.payload.UserDT;
import org.prashant.blog.blogapplicationapi.payload.UserPageResponse;
import org.prashant.blog.blogapplicationapi.payload.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long userId);
    UserPageResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    UserDT getUser(Long userId);
    UserDT updateUser(UpdateUserRequest request);

}
