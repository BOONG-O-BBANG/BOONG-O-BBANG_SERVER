package com.project.boongobbang.service;

import com.project.boongobbang.domain.dto.token.TokenResponseDto;
import com.project.boongobbang.domain.dto.user.UserSignInDto;
import com.project.boongobbang.domain.dto.user.UserSignUpDto;
import com.project.boongobbang.domain.dto.user.UserValidateDto;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.Role;
import com.project.boongobbang.exception.AppException;
import com.project.boongobbang.exception.ErrorCode;
import com.project.boongobbang.jwt.JwtUtils;
import com.project.boongobbang.repository.redis.RedisRefreshTokenRepository;
import com.project.boongobbang.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RedisRefreshTokenRepository refreshTokenRepository;

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

    public TokenResponseDto signIn(UserSignInDto dto) {
        String accessToken = (String) RequestContextHolder
                .currentRequestAttributes()
                .getAttribute(AUTHORIZATION, RequestAttributes.SCOPE_REQUEST);

        String refreshToken = (String) RequestContextHolder
                .currentRequestAttributes()
                .getAttribute("RefreshHeader", RequestAttributes.SCOPE_REQUEST);

        UserDetails user = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserNaverId(), dto.getUserEmail())
        ).getPrincipal();

        if (accessToken == null && refreshToken == null) {
            if (user != null) {
                return createTokens(user);
            }
            throw new AppException(ErrorCode.USER_ID_NOT_FOUND, "유저를 찾을 수 없습니다.");
        }

        return new TokenResponseDto(accessToken, refreshToken);
    }

    private TokenResponseDto createTokens(UserDetails user) {

        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.createRefreshToken();

        refreshTokenRepository.saveRefreshToken(accessToken, refreshToken);

        return new TokenResponseDto(accessToken, refreshToken);
    }

}
