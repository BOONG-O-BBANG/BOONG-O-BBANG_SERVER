package com.project.boongobbang.domain.entity.roommate;

import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.NotificationType;
import com.project.boongobbang.util.TimeStamped;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Notification extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    //알림의 주인 (체크하는 유저)
    @ManyToOne
    @JoinColumn(name = "check_user_id")
    private User checkUser;

    //알림에 태그된 유저 (checkUser 와 룸메이트가 되었거나, 요청을 보낸 사람)
    @ManyToOne
    @JoinColumn(name = "related_user_id")
    private User relatedUser;

    @Column(name = "notificationType")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "message")
    private String message;
}
