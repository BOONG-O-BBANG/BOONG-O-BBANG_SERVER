package com.project.boongobbang.domain.dto.user;

import com.project.boongobbang.domain.entity.user.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSimpleDto {
    private String userEmail;
    private String username;
    private int userAge;
    private String userPhotoUrl;
    private BigDecimal userAverageScore;

    public UserSimpleDto(User user){
        this.userEmail = user.getUserEmail();
        this.username = user.getUsername();
        this.userAge = LocalDate.now().getYear() - user.getUserBirth().getYear();
        this.userPhotoUrl = user.getUserPhotoUrl();
        this.userAverageScore = user.getAverageScore();

    }
}
