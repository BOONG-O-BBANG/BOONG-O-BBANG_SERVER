package com.project.boongobbang.domain.dto.user;


import com.project.boongobbang.domain.entity.user.User;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProfileDto {

    private String userPhotoUrl;
    private String userEmail;

    private String username;
    private String userMBTI;
    private String userBirth;
    private String userLocation;
    private String userCleanCount;

    private Boolean userHasPet;
    private Boolean userHasExperience;
    private Boolean userIsSmoker;
    private Boolean userIsNocturnal;

    private String userIntroduction;

    private BigDecimal userAverageScore;
    private String userMobile; //룸메이트 되었을 경우 나타남

    //자신의 프로필 조회
    public UserProfileDto(User user){
        this.userPhotoUrl = user.getUserPhotoUrl();
        this.userEmail = user.getUserEmail();

        this.username = user.getUsername();
        this.userMBTI = user.getUserMBTI().toString();
        this.userBirth = user.getUserBirth().toString();
        this.userLocation = user.getUserLocation().toString();
        this.userCleanCount = user.getUserCleanCount().toString();

        this.userHasPet = user.getUserHasPet();
        this.userHasExperience = user.getUserHasExperience();
        this.userIsSmoker = user.getUserIsSmoker();
        this.userIsNocturnal = user.getUserIsNocturnal();

        this.userIntroduction = user.getUserIntroduction();

        this.userAverageScore = user.getAverageScore();
        this.userMobile = user.getUserMobile();
    }

    //다른 유저 프로필 조회
    public UserProfileDto(User user, boolean isRoommate){
        this.userPhotoUrl = user.getUserPhotoUrl();

        this.username = user.getUsername();
        this.userMBTI = user.getUserMBTI().toString();
        this.userBirth = user.getUserBirth().toString();
        this.userLocation = user.getUserLocation().toString();
        this.userCleanCount = user.getUserCleanCount().toString();

        this.userHasPet = user.getUserHasPet();
        this.userHasExperience = user.getUserHasExperience();
        this.userIsSmoker = user.getUserIsSmoker();
        this.userIsNocturnal = user.getUserIsNocturnal();

        this.userIntroduction = user.getUserIntroduction();

        this.userAverageScore = user.getAverageScore();
        // 룸메이트 관계라면 userMobile 반환
        this.userMobile = isRoommate ? user.getUserMobile() : null;
    }
}

