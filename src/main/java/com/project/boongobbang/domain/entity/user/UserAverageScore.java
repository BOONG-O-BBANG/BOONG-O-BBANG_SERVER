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


}
