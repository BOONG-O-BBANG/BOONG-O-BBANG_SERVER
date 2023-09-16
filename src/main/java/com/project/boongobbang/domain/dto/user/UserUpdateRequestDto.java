package com.project.boongobbang.domain.dto.user;


import com.project.boongobbang.enums.CleanCount;
import com.project.boongobbang.enums.MBTI;
import com.project.boongobbang.enums.SeoulGu;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserUpdateRequestDto {
    @ApiModelProperty(position = 1, required = true, value = "유저 닉네임", example = "boong")
    @NotBlank(message = "닉네임을 입력해주세요")
    private String userNickname;

    @ApiModelProperty(position = 2, required = true, value = "유저 연락처", example = "010-2523-7481")
    @NotBlank(message = "연락처를 입력해주세요")
    private String userMobile;

    @ApiModelProperty(position = 3, required = true, value = "유저 주 청소횟수", example = "ZERO_TO_ONE")
    @NotBlank(message = "청소 횟수를 입력해주세요")
    private CleanCount userCleanCount;

    @ApiModelProperty(position = 4, required = true, value = "유저 희망 거주지", example = "은평구")
    @NotBlank(message = "희망 거주지를 입력해주세요")
    private SeoulGu userLocation;

    @ApiModelProperty(position = 5, required = true, value = "유저 MBTI", example = "ESTJ")
    @NotBlank(message = "MBTI를 입력해주세요")
    private MBTI userMbti;


    @ApiModelProperty(position = 6, required = true, value = "유저 반려동물 여부", example = "false")
    @NotBlank(message = "반려동물 여부를 입력해주세요")
    private Boolean userHasPet;

    @ApiModelProperty(position = 7, required = true, value = "유저 자취경험 여부", example = "false")
    @NotBlank(message = "자취경험 여부를 입력해주세요")
    private Boolean userHasExperience;

    @ApiModelProperty(position = 8, required = true, value = "유저 흡연 여부", example = "false")
    @NotBlank(message = "흡연 여부를 입력해주세요")
    private Boolean userIsSmoker;

    @ApiModelProperty(position = 9, required = true, value = "유저 야행성 여부", example = "false")
    @NotBlank(message = "야행성 여부를 입력해주세요")
    private Boolean userIsNocturnal;


    @ApiModelProperty(position = 10, required = true, value = "유저 소개", example = "반갑습니다. 내 이름은 정은기. 활기참.")
    private String userIntroduction;

    @ApiModelProperty(position = 11, required = true, value = "유저 사진 url", example = "")
    private String userPhotoUrl;
}
