package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.UserDTO;

import java.util.List;

public interface FollowService {
    void followUser(Long userToFollowId);
    void unfollowUser(Long userToUnfollowId);
    List<UserDTO> followers(Long userId);
}
