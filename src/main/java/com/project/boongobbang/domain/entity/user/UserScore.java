package com.project.boongobbang.domain.entity.user;

import com.project.boongobbang.util.TimeStamped;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserScore extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userScoreId;

    @ManyToOne
    @JoinColumn(name = "rating_user_id")
    private User ratingUser; //평가하는 User

    @ManyToOne
    @JoinColumn(name = "rated_user_id")
    private User ratedUser; //평가받는 User

    @Column(name = "is_rated")
    private boolean isRated;
    public void setIsRated(boolean isRated){
        this.isRated = isRated;
    }

    @Column(name = "score")
    private int score; //평가하는 사람이 평가받는 사람에게 준 점수
    public void setScore(int score) {
        this.score = score;
    }
}
