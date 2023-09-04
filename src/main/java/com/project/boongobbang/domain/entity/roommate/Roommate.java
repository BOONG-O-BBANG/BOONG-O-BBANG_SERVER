package com.project.boongobbang.domain.entity.roommate;

import com.project.boongobbang.domain.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Roommate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roommate_id")
    private Long roommateId;

    @ManyToOne
    @JoinColumn(name = "sent_user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "received_user_id")
    private User user2;

    private LocalDate startDate;
    public void start() {
        this.startDate = LocalDate.now();
    }

    private LocalDate endDate;
    public void end() {
        this.endDate = LocalDate.now();
    }
}
