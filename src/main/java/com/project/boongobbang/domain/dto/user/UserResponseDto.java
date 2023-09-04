package com.project.boongobbang.domain.dto.user;

import com.project.boongobbang.domain.dto.notification.NotificationResponseDto;
import com.project.boongobbang.domain.dto.roommate.RoommateResponseDto;
import com.project.boongobbang.domain.entity.user.User;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long userId;
    private String userNaverId;
    private String username;
    private String userNickname;
    private String userEmail;
    private String userBirth;
    private String userMobile;

    private String userGender;
    private String userCleanCount;
    private String userLocation;
    private String userMBTI;
    private String role;

    private Boolean userHasPet;
    private Boolean userHasExperience;
    private Boolean userIsSmoker;
    private Boolean userIsNocturnal;

    private String userIntroduction;
    private String userPhotoUrl;
    private BigDecimal userAverageScore;

    private String userType;

    private List<RoommateResponseDto> sentRoommateList;
    private List<RoommateResponseDto> receivedRoommateList;
    private List<NotificationResponseDto> notificationList;


    public UserResponseDto(User user){
        this.username = user.getUsername();
        this.userNaverId = user.getUserNaverId();
        this.userNickname = user.getUserNickname();
        this.userEmail = user.getUserEmail();
        this.userBirth = user.getUserBirth().toString();
        this.userMobile = user.getUserMobile();
        this.userGender = user.getUserGender().toString();
        this.userCleanCount = user.getUserCleanCount().toString();
        this.userMBTI = user.getUserMBTI().toString();
        this.role = user.getRole().toString();

        this.userHasPet = user.getUserHasPet();
        this.userHasExperience = user.getUserHasExperience();
        this.userIsSmoker = user.getUserIsSmoker();
        this.userIsNocturnal = user.getUserIsNocturnal();

        this.userIntroduction = user.getUserIntroduction();
        this.userPhotoUrl = user.getUserPhotoUrl();
        this.userAverageScore = user.getAverageScore();

        this.userType = user.getUserType().toString();
    }

    public UserResponseDto(User user,
                           List<RoommateResponseDto> sentRoommateDtoList,
                           List<RoommateResponseDto> receivedRoommateDtoList,
                           List<NotificationResponseDto> notificationDtoList){
        this.username = (user.getUsername());
        this.userNaverId = user.getUserNaverId();
        this.userNickname = (user.getUserNickname());
        this.userEmail = (user.getUserEmail());
        this.userBirth = (user.getUserBirth().toString());
        this.userMobile = (user.getUserMobile());
        this.userGender = (user.getUserGender().toString());
        this.userCleanCount = (user.getUserCleanCount().toString());
        this.userMBTI = (user.getUserMBTI().toString());
        this.role = (user.getRole().toString());

        this.userHasPet = (user.getUserHasPet());
        this.userHasExperience = (user.getUserHasExperience());
        this.userIsSmoker = (user.getUserIsSmoker());
        this.userIsNocturnal = (user.getUserIsNocturnal());

        this.userIntroduction = (user.getUserIntroduction());
        this.userPhotoUrl = (user.getUserPhotoUrl());
        this.userAverageScore = (user.getAverageScore());

        this.userType = user.getUserType().toString();

        this.sentRoommateList = (sentRoommateDtoList);
        this.receivedRoommateList = (receivedRoommateDtoList);
        this.notificationList = (notificationDtoList);
    }
}

