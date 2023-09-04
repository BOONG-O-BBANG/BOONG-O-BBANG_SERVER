package com.project.boongobbang.domain.entity.user;

import com.project.boongobbang.domain.entity.roommate.Notification;
import com.project.boongobbang.domain.entity.roommate.Roommate;
import com.project.boongobbang.enums.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class User {
    @Id
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_naver_id", unique = true)
    private String userNaverId;

    @Column(name = "username")
    private String userName;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_birth")
    private LocalDate userBirth;

    @Column(name = "user_mobile")
    private String userMobile;

    @Enumerated(EnumType.STRING)
    private Gender userGender;

    @Enumerated(EnumType.ORDINAL)
    private CleanCount userCleanCount;

    @Enumerated(EnumType.STRING)
    private SeoulGu userLocation;

    @Enumerated(EnumType.STRING)
    private MBTI userMBTI;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "user_has_pet")
    private Boolean userHasPet;

    @Column(name = "user_has_experience")
    private Boolean userHasExperience;

    @Column(name = "user_is_smoker")
    private Boolean userIsSmoker;

    @Column(name = "user_is_nocturnal")
    private Boolean userIsNocturnal;

    @Column(name = "user_introduction")
    private String userIntroduction;

    @Column(name = "user_photo")
    private String userPhotoUrl;

    @Embedded
    UserAverageScore userAverageScore;

    //MAPPING

    //Roommate
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Roommate> sentRoommateList;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Roommate> receivedRoommateList;

    //Notification
    @OneToMany(mappedBy = "checkUser")
    private List<Notification> receivedNotificationList;

    //UserScore
    @OneToMany(mappedBy = "ratingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserScore> gaveScoreList;

    @OneToMany(mappedBy = "ratedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserScore> receivedScoreList;


    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;
    public void setUserType(String userType){
        this.userType = UserType.valueOf(userType);
    }

    @Column(name = "is_paired")
    private boolean isPaired;
    public void setIsPaired(boolean isPaired){
        this.isPaired = isPaired;
    }


}
