package com.project.boongobbang.domain.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInDto {
    @ApiModelProperty(position = 1, required = true, value = "유저 네이버 아이디", example = "eunki96")
    private String userNaverId;

    @ApiModelProperty(position = 4, required = true, value = "유저 이메일", example = "eunki96@naver.com")
    private String userEmail;
}
