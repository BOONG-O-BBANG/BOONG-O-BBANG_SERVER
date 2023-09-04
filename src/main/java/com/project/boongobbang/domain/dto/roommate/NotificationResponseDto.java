package com.project.boongobbang.domain.dto.roommate;

import com.project.boongobbang.domain.entity.roommate.Notification;
import com.project.boongobbang.enums.NotificationType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private Long notificationId;
    private String relatedUserEmail; //프로필 상세보기 위함
    private NotificationType notificationType;
    private String message;

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.relatedUserEmail = notification.getRelatedUser().getUserEmail();
        this.notificationType = notification.getNotificationType();
        this.message = notification.getMessage();
    }
}

