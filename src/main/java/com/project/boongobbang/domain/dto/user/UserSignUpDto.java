package com.project.boongobbang.domain.dto.user;

import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserSignUpDto {

    @NotBlank(message = "네이버 아이디를 입력해주세요")
    @ApiModelProperty(position = 1, required = true, value = "유저 네이버 아이디", example = "eunki96")
    private String userNaverId;

    @ApiModelProperty(position = 2, required = true, value = "유저 실명", example = "돌자반")
    @NotBlank(message = "이름을 입력해주세요")
    private String username;

    @ApiModelProperty(position = 3, required = true, value = "유저 닉네임", example = "boong")
    @NotBlank(message = "닉네임을 입력해주세요")
    private String userNickname;

    @ApiModelProperty(position = 4, required = true, value = "유저 이메일", example = "eunki96@naver.com")
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식이 잘못되었습니다")
    @Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$", message = "이메일 형식이 잘못되었습니다")
    private String userEmail;

    @ApiModelProperty(position = 5, required = true, value = "유저 생일", example = "1996-02-29")
    @NotBlank(message = "생일을 입력해주세요")
    private LocalDate userBirth;

    @ApiModelProperty(position = 6, required = true, value = "유저 연락처", example = "010-2523-7481")
    @NotBlank(message = "연락처를 입력해주세요")
    private String userMobile;


    @ApiModelProperty(position = 7, required = true, value = "유저 성별", example = "MAN")
    @NotBlank(message = "성별을 입력해주세요")
    private Gender userGender;

    @ApiModelProperty(position = 8, required = true, value = "유저 주 청소횟수", example = "ONE_TO_TWO")
    @NotBlank(message = "청소 횟수를 입력해주세요")
    private CleanCount userCleanCount;

    @ApiModelProperty(position = 9, required = true, value = "유저 희망 거주지", example = "은평구")
    @NotBlank(message = "희망 거주지를 입력해주세요")
    private SeoulGu userLocation;

    @ApiModelProperty(position = 10, required = true, value = "유저 MBTI", example = "ESTJ")
    @NotBlank(message = "MBTI를 입력해주세요")
    private MBTI userMBTI;


    @ApiModelProperty(position = 11, required = true, value = "유저 반려동물 여부", example = "false")
    @NotBlank(message = "반려동물 여부를 입력해주세요")
    private Boolean userHasPet;

    @ApiModelProperty(position = 12, required = true, value = "유저 자취경험 여부", example = "false")
    @NotBlank(message = "자취경험 여부를 입력해주세요")
    private Boolean userHasExperience;

    @ApiModelProperty(position = 13, required = true, value = "유저 흡연 여부", example = "false")
    @NotBlank(message = "흡연 여부를 입력해주세요")
    private Boolean userIsSmoker;

    @ApiModelProperty(position = 14, required = true, value = "유저 야행성 여부", example = "false")
    @NotBlank(message = "야행성 여부를 입력해주세요")
    private Boolean userIsNocturnal;


    @ApiModelProperty(position = 15, required = true, value = "유저 소개", example = "반갑습니다. 내 이름은 정은기. 활기참.")
    private String userIntroduction;


    public User toEntity(){
        return User.builder()
                .username(username)
                .userNaverId(userNaverId)
                .userNickname(userNickname)
                .userEmail(userEmail)
                .userBirth(userBirth)
                .userMobile(userMobile)

                .userGender(userGender)
                .userCleanCount(userCleanCount)
                .userLocation(userLocation)
                .userMBTI(userMBTI)
                .role(Role.ROLE_USER)

                .userHasPet(userHasPet)
                .userHasExperience(userHasExperience)
                .userIsSmoker(userIsSmoker)
                .userIsNocturnal(userIsNocturnal)

                .userIntroduction(userIntroduction)
                .userPhotoUrl("empty")


                .ratedCount(0L)
                .averageScore(BigDecimal.valueOf(-1))

                .sentRoommateList(new ArrayList<>())
                .receivedRoommateList(new ArrayList<>())
                .receivedNotificationList(new ArrayList<>())
                .gaveScoreList(new ArrayList<>())
                .receivedScoreList(new ArrayList<>())

                .userType(null)
                .isPaired(false)
                .build();
    }
}
