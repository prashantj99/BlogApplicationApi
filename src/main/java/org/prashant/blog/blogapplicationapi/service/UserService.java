package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.*;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long userId);
    UserPageResponse getUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    UserDT getUser(Long userId);
    UserDT updateUser(UpdateUserRequest request);

    void subscribeToCategory(Long userId, Long categoryId);
    PostPageResponse getPostsFromSubscribedCategories(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
