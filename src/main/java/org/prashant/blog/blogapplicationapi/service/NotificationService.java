package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.entities.Notification;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.payload.NotificationPageResponse;

public interface NotificationService {
    NotificationPageResponse getNotificationsForUser(int pageNumber, int pageSize);
    void saveNotification(User user, Notification notification);
    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
}
