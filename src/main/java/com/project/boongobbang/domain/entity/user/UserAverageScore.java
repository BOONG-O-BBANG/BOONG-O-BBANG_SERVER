package com.project.boongobbang.domain.entity.user;

import com.project.boongobbang.util.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Embeddable
public class UserAverageScore extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAverageScoreId;

    @OneToOne
    private User user;

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
}
