package com.project.boongobbang.domain.dto.roommate;

import com.project.boongobbang.domain.entity.roommate.Roommate;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoommateResponseDto {
    private Long roommateId;
    private String userEmail1;
    private String userEmail2;
    private LocalDate startDate;
    private LocalDate endDate;

    public RoommateResponseDto(Roommate roommate){
        this.roommateId = roommate.getRoommateId();
        this.userEmail1 = roommate.getUser1().getUserEmail();
        this.userEmail2 = roommate.getUser2().getUserEmail();
        this.startDate = roommate.getStartDate();
        this.endDate = roommate.getEndDate();
    }
}

