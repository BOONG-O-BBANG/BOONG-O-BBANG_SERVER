package com.project.boongobbang.repository.roommate;

import com.project.boongobbang.domain.entity.roommate.Notification;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Notification findNotificationByNotificationId(Long NotificationId);

    List<Notification> findNotificationsByCheckUser(User user);
    Page<Notification> findNotificationsByCheckUser(User user, Pageable pageable);

    void deleteNotificationByNotificationId(Long notificationId);
    
    @Query("SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END FROM Notification n WHERE n.checkUser = :checkUser AND n.relatedUser = :relatedUser AND n.notificationType = :type")
    Boolean existsByCheckUserAndRelatedUser(@Param("checkUser") User checkUser, @Param("relatedUser") User relatedUser, @Param("type") NotificationType type);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.checkUser.userEmail = :userEmail OR n.relatedUser.userEmail = :userEmail")
    void deleteNotificationsByUserEmail(@Param("userEmail") String userEmail);
}
