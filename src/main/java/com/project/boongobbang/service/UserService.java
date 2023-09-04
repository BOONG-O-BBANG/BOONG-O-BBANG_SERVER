package com.project.boongobbang.service;

import com.project.boongobbang.domain.dto.token.TokenResponseDto;
import com.project.boongobbang.domain.dto.user.UserSignInDto;
import com.project.boongobbang.domain.dto.user.UserSignUpDto;
import com.project.boongobbang.domain.dto.user.UserSimpleDto;
import com.project.boongobbang.domain.dto.user.UserValidateDto;
import com.project.boongobbang.domain.entity.roommate.Notification;
import com.project.boongobbang.domain.entity.roommate.Roommate;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.domain.entity.user.UserScore;
import com.project.boongobbang.enums.Role;
import com.project.boongobbang.exception.AppException;
import com.project.boongobbang.exception.ErrorCode;
import com.project.boongobbang.jwt.JwtUtils;
import com.project.boongobbang.repository.redis.RedisRefreshTokenRepository;
import com.project.boongobbang.repository.roommate.NotificationRepository;
import com.project.boongobbang.repository.roommate.RoommateRepository;
import com.project.boongobbang.repository.user.UserRepository;
import com.project.boongobbang.repository.user.UserScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.project.boongobbang.enums.CleanCount.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoommateRepository roommateRepository;
    private final UserScoreRepository userScoreRepository;
    private final AuthenticationManager authenticationManager;
    private final NotificationRepository notificationRepository;
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
                        .username(dto.getUsername())
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

    //유저 생성/수정 시 UserType 설정
    public String determineUserType(User user) {

        List<Function<User, String>> userCharacteristics = Arrays.asList(
                u -> {
                    if (u.getUserCleanCount() == ZERO_TO_ONE) {
                        return "CLEAN_0_1_";
                    } else if (u.getUserCleanCount() == TWO_TO_FOUR) {
                        return "CLEAN_2_4_";
                    } else if (u.getUserCleanCount() == MORE_THAN_FIVE) {
                        return "CLEAN_MORE_5_";
                    } else {
                        throw new IllegalArgumentException("일치하는 유저타입이 없습니다");
                    }
                },
                u -> {
                    String mbti = u.getUserMBTI().toString();
                    return mbti.charAt(0) + "_" + mbti.charAt(2) + "_";
                },
                u -> u.getUserIsSmoker() ? "SMOKER_" : "NON_SMOKER_",
                u -> u.getUserIsNocturnal() ? "NOCTURNAL" : "DIURNAL"
        );

        String userTypeStr = userCharacteristics.stream()
                .map(f -> f.apply(user))
                .collect(Collectors.joining());

        return userTypeStr;
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







    //식별자로 User 검색
    public User findUserByUserId(String userEmail){
        User user = userRepository.findUserByUserEmail(userEmail)
                .orElseThrow(
                        () ->  new RuntimeException("[Error] 존재하지 않는 유저입니다")
                );
        return user;
    }
    //식별자로 Notification 검색
    public Notification findNotificationByNotificationId(Long notificationtId){
        Notification notification;
        try{
            notification = notificationRepository.findNotificationByNotificationId(notificationtId);
        }catch(RuntimeException e){
            throw new RuntimeException("[Error] 존재하지 않는 알림입니다.");
        }
        return notification;
    }
    //식별자로 UserScore 검색
    public UserScore findUserScoreByUserScoreId(Long userScoreId){
        UserScore userScore;
        try{
            userScore = userScoreRepository.findUserScoreByUserScoreId(userScoreId);
        }catch(RuntimeException e){
            throw new RuntimeException("[Error] 존재하지 않는 알림입니다.");
        }
        return userScore;
    }
    //식별자로 Roommate 검색
    public Roommate findRoommateByRoommateId(Long roommateId){
        Roommate roommate;
        try{
            roommate = roommateRepository.findRoommateByRoommateId(roommateId);
        }catch(RuntimeException e){
            throw new RuntimeException("[Error] 존재하지 않는 룸메이트 관계입니다.");
        }
        return roommate;
    }
    //구성 User 의 Email 로 Roommate 검색
    public Roommate findRoommateByUsers(String userEmail1, String userEmail2){
        Roommate roommate;
        try{
            roommate = roommateRepository.findRoommateByUsers(userEmail1, userEmail2);
        }catch(RuntimeException e){
            return null;
        }
        return roommate;
    }
    //전체 User 페이지로 검색
    public List<UserSimpleDto> getUsersByPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.stream()
                .map(user -> new UserSimpleDto(user))
                .collect(Collectors.toList());
    }
}
