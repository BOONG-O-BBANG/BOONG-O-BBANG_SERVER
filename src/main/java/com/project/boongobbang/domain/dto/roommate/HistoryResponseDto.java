package com.project.boongobbang.domain.dto.roommate;

import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.domain.entity.user.UserScore;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HistoryResponseDto {
    Long userScoreId;
    String roommateEmail;
    String roommateName;
    int userScore;

    public HistoryResponseDto(UserScore userScore, User roommate){
        this.userScoreId = userScore.getUserScoreId();
        this.userScore = userScore.getScore();
        this.roommateEmail = userScore.getRatedUser().getUserEmail();
        this.roommateName = roommate.getUsername();
    }
}

