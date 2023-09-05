package com.project.boongobbang.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.boongobbang.domain.dto.token.TokenResponseDto;
import com.project.boongobbang.domain.dto.user.*;
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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.project.boongobbang.enums.CleanCount.*;
import static com.project.boongobbang.enums.UserType.CLEAN_0_1_E_T_SMOKER_NOCTURNAL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private  String bucketName = "boong-o-bbang-img";
    private final AmazonS3Client amazonS3Client;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RedisRefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;
    private final RoommateRepository roommateRepository;
    private final UserScoreRepository userScoreRepository;
    private final NotificationRepository notificationRepository;


    public Boolean validate(UserValidateDto dto) {
        return userRepository.existsByUserNaverId(dto.getUserNaverId());
    }

    public String getLoginUserInfo(){
        return jwtUtils.extractUsername(
                (String) RequestContextHolder
                .currentRequestAttributes()
                .getAttribute(AUTHORIZATION, RequestAttributes.SCOPE_REQUEST)
        );
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
                        .userPhotoUrl("empty")
                        .role(Role.ROLE_USER)
                        .userType(CLEAN_0_1_E_T_SMOKER_NOCTURNAL) //임시
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

        UserDetails user = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserNaverId(), "")
        ).getPrincipal();

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "[Error] 유저 존재하지 않습니다");
        }

        //INVALID는 처리된 이후. 즉, 유효성 검증을 끝낸 유효한 access token이다.
        if (accessToken != null) {
            return new TokenResponseDto(accessToken);
        }
        refreshTokenRepository.saveRefreshToken(user.getUsername(), jwtUtils.createRefreshToken());

        return new TokenResponseDto(jwtUtils.generateToken(user));
    }

    @Transactional
    public User updateUser(String userEmail, UserUpdateRequestDto dto) {
        User user = findUserByUserEmail(userEmail);

        if (!Objects.equals(user.getUserNickname(), dto.getUserNickname())) {
            user.setUserNickname(dto.getUserNickname());
        }
        if (!Objects.equals(user.getUserCleanCount(), dto.getUserCleanCount())) {
            user.setUserCleanCount(dto.getUserCleanCount());
        }
        if (!Objects.equals(user.getUserLocation(), dto.getUserLocation())) {
            user.setUserLocation(dto.getUserLocation());
        }
        if (!Objects.equals(user.getUserMBTI(), dto.getUserMbti())) {
            user.setUserMBTI(dto.getUserMbti());
        }
        if (!Objects.equals(user.getUserHasPet(), dto.getUserHasPet())) {
            user.setUserHasPet(dto.getUserHasPet());
        }
        if (!Objects.equals(user.getUserHasExperience(), dto.getUserHasExperience())) {
            user.setUserHasExperience(dto.getUserHasExperience());
        }
        if (!Objects.equals(user.getUserIsSmoker(), dto.getUserIsSmoker())) {
            user.setUserIsSmoker(dto.getUserIsSmoker());
        }
        if (!Objects.equals(user.getUserIsNocturnal(), dto.getUserIsNocturnal())) {
            user.setUserIsNocturnal(dto.getUserIsNocturnal());
        }
        if (!Objects.equals(user.getUserIntroduction(), dto.getUserIntroduction())) {
            user.setUserIntroduction(dto.getUserIntroduction());
        }
        if (!Objects.equals(user.getUserPhotoUrl(), dto.getUserPhotoUrl())) {
            user.setUserPhotoUrl(dto.getUserPhotoUrl());
        }

        user.setUserType(determineUserType(user));

        userRepository.save(user);
        return user;
    }

    //유저 삭제
    @Transactional
    public UserProfileDto deleteUser(String userEmail) {
        User user = findUserByUserEmail(userEmail);
        UserProfileDto dto = new UserProfileDto(user);
        userRepository.deleteUserByUserEmail(userEmail);
        return dto;
    }


    //식별자로 User 검색
    public User findUserByUserEmail(String userEmail) {
        User user = userRepository.findUserByUserEmail(userEmail)
                .orElseThrow(
                        () -> new RuntimeException("[Error] 존재하지 않는 유저입니다")
                );
        return user;
    }

    //네이버 ID 로 User 검색
    public User findUserByUserNaverId(String userNaverId){
        User user = userRepository.findUserByUserNaverId(userNaverId)
                .orElseThrow(
                        () -> new RuntimeException("[Error] 존재하지 않는 유저입니다")
                );
        return user;
    }

    //식별자로 Notification 검색
    public Notification findNotificationByNotificationId(Long notificationtId) {
        Notification notification;
        try {
            notification = notificationRepository.findNotificationByNotificationId(notificationtId);
        } catch (RuntimeException e) {
            throw new RuntimeException("[Error] 존재하지 않는 알림입니다.");
        }
        return notification;
    }

    //식별자로 UserScore 검색
    public UserScore findUserScoreByUserScoreId(Long userScoreId) {
        UserScore userScore;
        try {
            userScore = userScoreRepository.findUserScoreByUserScoreId(userScoreId);
        } catch (RuntimeException e) {
            throw new RuntimeException("[Error] 존재하지 않는 알림입니다.");
        }
        return userScore;
    }

    //식별자로 Roommate 검색
    public Roommate findRoommateByRoommateId(Long roommateId) {
        Roommate roommate;
        try {
            roommate = roommateRepository.findRoommateByRoommateId(roommateId);
        } catch (RuntimeException e) {
            throw new RuntimeException("[Error] 존재하지 않는 룸메이트 관계입니다.");
        }
        return roommate;
    }

    //구성 User 의 Email 로 Roommate 검색
    public Roommate findRoommateByUsers(String userEmail1, String userEmail2) {
        Roommate roommate;
        try {
            roommate = roommateRepository.findRoommateByUsers(userEmail1, userEmail2);
        } catch (RuntimeException e) {
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



    /* DTO 반환 */

    //User(본인) 입력받아 UserProfileDto 반환
    //무조건 모든 정보를 공개
    public UserProfileDto returnMyProfileDto(User user) {
        UserProfileDto dto = new UserProfileDto(user);
        return dto;
    }

    //User 입력받아 UserProfileDto 반환
    public UserProfileDto returnUserProfileDto(User me, User user) {
        //룸메이트라면 userMobile 까지 공개
        boolean isRoommate = (roommateRepository.findRoommateByUsers(me.getUserEmail(), user.getUserEmail()) == null) ? false : true;
        UserProfileDto dto = new UserProfileDto(user, isRoommate);
        return dto;
    }

    //User 입력받아 UserSimpleDto 반환
    public UserSimpleDto returnUserSimpleDto(User user) {
        UserSimpleDto dto = new UserSimpleDto(user);
        return dto;
    }

    //User 입력받아 UserResponseDto 반환
    public UserResponseDto returnUserDto(User user) {
        UserResponseDto dto = new UserResponseDto(user);
        return dto;
    }








    //사진 관련

    //사진 저장
    @org.springframework.transaction.annotation.Transactional
    public String savePhoto(MultipartFile file, User user) {

        log.info("[UserService] savePhoto");
        log.info("user.getUserPhotoUrl = {}", user.getUserPhotoUrl());
        if(!(user.getUserPhotoUrl().equals("empty"))){
            log.info("[UserService] updateUserProfilePhoto : if(!(user.getUserPhotoUrl() == \"empty\"))");
            deleteUserProfilePhoto(user);
        }

        String filename = file.getOriginalFilename();
        String storedFileName = getUserProfileFileName(filename);

        String photoUrl = uploadFileToS3(storedFileName, file);
        user.setUserPhotoUrl(photoUrl);

        return photoUrl;
    }

    //유저 프로필사진 삭제
    @Transactional
    public User deleteUserProfilePhoto(User user) {
        log.info("[UserService] deleteUserProfilePhoto");

        String fileName = extractProfilePhotoFileName(user);
        log.info("[UserService] deleteUserProfilePhoto : fileName = {}", fileName);

        user.setUserPhotoUrl("empty");
        deleteFileFromS3(fileName);

        return userRepository.save(user);
    }

    //유저 프로필 사진 이름 생성
    public String getUserProfileFileName(String fileName){
        return  "user_profile/"  + UUID.randomUUID()+ fileName.substring(fileName.lastIndexOf('.'));
    }

    //유저의 프로필 사진 이름 추출
    public static String extractProfilePhotoFileName(User user) {
        log.info("[UserService] extractProfilePhotoFileName");
        String path = null;

        try {
            log.info("[???] = {}", user.getUserPhotoUrl());
            URL url = new URL(user.getUserPhotoUrl());
            log.info("url = {}", url);
            path = url.getPath();
            log.info("path = {}", path);

            if (path.startsWith("/")) {
                path = path.substring(1);
                log.info("path = {}", path);
            }
        } catch (Exception e) {
            throw new RuntimeException("RuntimeException 발생");
        }
        log.info("path = {}", path);
        return path;
    }

    //S3에 사진 업로드
    public String uploadFileToS3(String fileName, MultipartFile file) {

        try {
            File convertedFile = convertFile(file);
            String uploadingFileName = fileName;

            amazonS3Client.putObject(new PutObjectRequest(bucketName, uploadingFileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            String url = amazonS3Client.getUrl(bucketName, uploadingFileName).toString();
            return url;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    //S3 에서 사진 삭제
    public void deleteFileFromS3(String fileName) {
        log.info("[UserService] deleteFileFromS3");
        try {
            amazonS3Client.deleteObject(bucketName, fileName);
        } catch (SdkClientException e) {
            throw new RuntimeException();
        }
    }

    //MultipartFile을 File로 전환
    private File convertFile(MultipartFile file) throws IOException {
        File convertingFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertingFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return convertingFile;
    }

    //사진 저장 이름 생성
    public String getStoredFileName(String fileName){
        return UUID.randomUUID() + fileName.substring(fileName.lastIndexOf('.'));
    }














    //
    public TokenResponseDto reissue(ReIssueDto dto) {
        String findRefreshToken = refreshTokenRepository.findRefreshToken(dto.getUserNaverId());

        if (findRefreshToken == null) {
            throw new AppException(ErrorCode.USER_ID_NOT_FOUND, "[Error] 재발행이 불가합니다.");
        }

        if (jwtUtils.isTokenValid(findRefreshToken)) {
            UserDetails user = (UserDetails) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUserNaverId(), "")
            ).getPrincipal();

            return new TokenResponseDto(jwtUtils.generateToken(user));
        }else {
            throw new AppException(ErrorCode.EXPIRED_TOKEN, "[Error] 재로그인 하십시오.");
        }

    }
}
