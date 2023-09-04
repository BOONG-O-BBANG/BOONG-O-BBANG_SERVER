package com.project.boongobbang.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInDto {
    private String userNaverId;
    private String userEmail;
}
