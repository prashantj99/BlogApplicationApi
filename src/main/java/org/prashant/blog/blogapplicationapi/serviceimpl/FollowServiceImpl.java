package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.prashant.blog.blogapplicationapi.entities.Notification;
import org.prashant.blog.blogapplicationapi.entities.NotificationType;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.exceptions.UnAuthorizedOperationExcpetion;
import org.prashant.blog.blogapplicationapi.payload.UserDTO;
import org.prashant.blog.blogapplicationapi.repository.UserRepository;
import org.prashant.blog.blogapplicationapi.service.FollowService;
import org.prashant.blog.blogapplicationapi.service.NotificationService;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void followUser(Long userToFollowId) {
        User loggedInUser = userService.getLoggedInUser()
                .orElseThrow(() -> new UnAuthorizedOperationExcpetion("Unauthorized Access to Resource!!!"));

        User userToFollow = userRepository.findById(userToFollowId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userToFollowId.toString()));

        if(loggedInUser.getUserId().equals(userToFollowId)){
            return;
        }

        // Avoid adding duplicates
        if (loggedInUser.getFollowedUsers().contains(userToFollow)) {
            return;
        }

        loggedInUser.getFollowedUsers().add(userToFollow);
        userRepository.save(loggedInUser);

        // Create and save a notification of follow user
        Notification notification = new Notification();
        notification.setUser(userToFollow);
        notification.setType(NotificationType.FOLLOW);
        notification.setContent("User " + loggedInUser.getName() + " followed you.");
        notificationService.saveNotification(userToFollow, notification);
    }

    @Override
    @Transactional
    public void unfollowUser(Long userToUnfollowId) {
        User loggedInUser = userService.getLoggedInUser()
                .orElseThrow(() -> new UnAuthorizedOperationExcpetion("Unauthorized Access to Resource!!!"));

        User userToUnfollow = userRepository.findById(userToUnfollowId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userToUnfollowId.toString()));

        if(loggedInUser.getUserId().equals(userToUnfollowId)){
            return;
        }

        if (!loggedInUser.getFollowedUsers().contains(userToUnfollow)) {
            return;
        }

        loggedInUser.getFollowedUsers().remove(userToUnfollow);
        userRepository.save(loggedInUser);
    }

    @Override
    @Transactional
    public List<UserDTO> followers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.toString()));

        return user.getFollowers().stream()
                .map(UserDTO::new)
                .toList();
    }
}
