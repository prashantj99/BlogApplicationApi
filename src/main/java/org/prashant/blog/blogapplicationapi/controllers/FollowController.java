package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.payload.UserDTO;
import org.prashant.blog.blogapplicationapi.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/connect")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> followUser(@PathVariable("userId") Long userToFollowId) {
        followService.followUser(userToFollowId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable("userId") Long userToUnfollowId) {
        followService.unfollowUser(userToUnfollowId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getUserFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.followers(userId));
    }
}

