package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.NotificationType;

import java.util.Date;

public record NotificationDTO(Long id,
                              String message,
                              NotificationType type,
                              Boolean isRead,
                              Date createdAt) {
}
