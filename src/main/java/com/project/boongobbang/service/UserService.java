package com.project.boongobbang.service;

import com.project.boongobbang.domain.dto.user.UserSignUpDto;
import com.project.boongobbang.domain.dto.user.UserValidateDto;
import com.project.boongobbang.domain.entity.User;
import com.project.boongobbang.enums.Role;
import com.project.boongobbang.exception.AppException;
import com.project.boongobbang.exception.ErrorCode;
import com.project.boongobbang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Boolean validate(UserValidateDto dto) {
        return userRepository.existsByUserNaverId(dto.getUserNaverId());
    }

    public void signUp(UserSignUpDto dto) {
        userRepository.findByUserNaverId(dto.getUserNaverId()).ifPresent(user -> {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS, "이미 존재하는 회원입니다.");
        });

        userRepository.save(
                User.builder()
                        .userNaverId(dto.getUserNaverId())
                        .userName(dto.getUserName())
                        .userNickname(dto.getUserNickname())
                        .userEmail(dto.getUserEmail())
                        .userBirth(dto.getUserBirth())
                        .userMobile(dto.getUserMobile())
                        .userGender(dto.getUserGender())
                        .userCleanCount(dto.getUserCleanCount())
                        .userLocation(dto.getUserLocation())
                        .userMBTI(dto.getUserMBTI())
                        .userHasPet(dto.getUserHasPet())
                        .userHasExperience(dto.getUserHasExperience())
                        .userIsNocturnal(dto.getUserIsNocturnal())
                        .userIntroduction(dto.getUserIntroduction())
                        .userPhotoUrl(dto.getUserPhotoUrl())
                        .role(Role.ROLE_USER)
                        .build());
    }

}
