package com.campusfind.service;

import com.campusfind.dto.NotificationResponse;
import com.campusfind.entity.User;

import java.util.List;

/**
 * Interface demonstrating OOP Abstraction and loose coupling.
 */
public interface NotificationService {

    void sendNotification(User recipient, String message, Long relatedItemId, Long relatedClaimId);

    List<NotificationResponse> getUserNotifications(User recipient);

    long getUnreadCount(User recipient);

    void markAsRead(Long notificationId, User currentUser);

    void markAllAsRead(User currentUser);
}
