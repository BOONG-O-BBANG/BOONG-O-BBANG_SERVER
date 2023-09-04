package com.project.boongobbang.domain.dto.user;

import com.project.boongobbang.domain.entity.user.User;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSimpleDto {
    private String userEmail;
    private String username;
    private String userBirth;
    private String userPhotoUrl;
    private BigDecimal userAverageScore;

    public UserSimpleDto(User user){
        this.userEmail = user.getUserEmail();
        this.username = user.getUsername();
        this.userBirth = user.getUserBirth().toString();
        this.userPhotoUrl = user.getUserPhotoUrl();
        this.userAverageScore = user.getAverageScore();

    }
}
