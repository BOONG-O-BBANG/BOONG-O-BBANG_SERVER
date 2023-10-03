package com.project.boongobbang.domain.entity.user;

import com.project.boongobbang.domain.entity.roommate.Notification;
import com.project.boongobbang.domain.entity.roommate.Roommate;
import com.project.boongobbang.enums.*;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Table(indexes = {
        @Index(name = "idx_user_type", columnList = "user_type"),
        @Index(name = "idx_user_location", columnList = "user_location"),
        @Index(name = "idx_user_gender", columnList = "user_gender"),
        @Index(name = "idx_user_birth", columnList = "user_birth"),
        @Index(name = "idx_is_paired", columnList = "is_paired")
})
public class User {
    @Id
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_naver_id", unique = true)
    private String userNaverId;

    @Column(name = "username")
    private String username;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_birth")
    private LocalDate userBirth;

    @Column(name = "user_mobile")
    private String userMobile;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    private Gender userGender;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_clean_count")
    private CleanCount userCleanCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_location")
    private SeoulGu userLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_mbti")
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


    //AVERAGE_SCORE
    @Column(name = "average_score",precision = 2, scale = 1) // 전체 2자리 중 소수점 1자리
    private BigDecimal averageScore;
    public void setAverageScore(BigDecimal averageScore){
        this.averageScore = averageScore;
    }

    @Column(name = "rated_count")
    private Long ratedCount;
    public void setRatedCount(Long ratedCount) {
        this.ratedCount = ratedCount;
    }


    //MAPPING

    //Roommate
    @OneToMany(cascade = CascadeType.ALL)
    private List<Roommate> sentRoommateList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Roommate> receivedRoommateList = new ArrayList<>();

    //Notification
    @OneToMany(cascade = CascadeType.ALL)
    private List<Notification> receivedNotificationList = new ArrayList<>();

    //UserScore
    @OneToMany(cascade = CascadeType.ALL)
    private List<UserScore> gaveScoreList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserScore> receivedScoreList = new ArrayList<>();


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


    @Version
    private Long version;
}
