package com.project.boongobbang.domain.dto.user;

import com.project.boongobbang.enums.CleanCount;
import com.project.boongobbang.enums.Gender;
import com.project.boongobbang.enums.MBTI;
import com.project.boongobbang.enums.SeoulGu;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserSignUpDto {

    private String userNaverId;

    private String userName;

    private String userNickname;

    private String userEmail;

    private LocalDate userBirth;

    private String userMobile;

    private Gender userGender;

    private CleanCount userCleanCount;

    private SeoulGu userLocation;

    private MBTI userMBTI;

    private Boolean userHasPet;

    private Boolean userHasExperience;

    private Boolean userIsSmoker;

    private Boolean userIsNocturnal;

    private String userIntroduction;

    private String userPhotoUrl;
}
