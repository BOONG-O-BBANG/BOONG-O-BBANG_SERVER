package com.project.boongobbang.controller;


import com.project.boongobbang.domain.dto.token.TokenResponseDto;
import com.project.boongobbang.domain.dto.user.*;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;


@Api(tags = "유저 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //유저 CRUD

    @ApiOperation("유저 가입")
    @ApiResponses(value={
            @ApiResponse(code = 201,
                    message = "USER_SIGN_UP",
                    response = UserResponseDto.class),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 400,
                    message = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(
            @RequestBody UserSignUpDto dto) {
        userService.signUp(dto);

        return ResponseEntity.ok().body("유저가입이 되었습니다.");
    }

    @ApiOperation("유저 로그인")
    @ApiResponses(value={
            @ApiResponse(code = 201,
                    message = "USER_SIGN_UP",
                    response = UserResponseDto.class),
            @ApiResponse(code = 400,
                    message = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody UserSignInDto dto) {
        TokenResponseDto tokenResponseDto = userService.signIn(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());

        return ResponseEntity.ok().headers(headers).body("로그인 성공");
    }

    @ApiOperation("유저 상세 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/detail")
    public ResponseEntity<UserResponseDto> getUserDetail() {
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        UserResponseDto userResponseDto = userService.returnUserDto(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @ApiOperation("로그인 유저 프로필 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_PROFILE_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getMyProfile() {
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        UserProfileDto userProfileDto = userService.returnMyProfileDto(user);
        return new ResponseEntity<>(userProfileDto, HttpStatus.CREATED);
    }

    @ApiOperation("다른 유저 프로필 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_PROFILE_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{otherUserEmail}")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @PathVariable String otherUserEmail) {
        String userNaverId = userService.getLoginUserInfo();
        User me = userService.findUserByUserNaverId(userNaverId);

        User user = userService.findUserByUserEmail(otherUserEmail);
        UserProfileDto userProfileDto = userService.returnUserProfileDto(me, user);
        return new ResponseEntity<>(userProfileDto, HttpStatus.CREATED);
    }


    @ApiOperation("로그인 유저 정보 수정")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_UPDATED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    public ResponseEntity<UserResponseDto> updateUser(
            @RequestBody UserUpdateRequestDto dto) {
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);
        user = userService.updateUser(user, dto);

        UserResponseDto userResponseDto = userService.returnUserDto(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @ApiOperation("로그인 유저 스스로 탈퇴 (임시)")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_DELETED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    public ResponseEntity<UserProfileDto> deleteUser() {
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        UserProfileDto userProfileDto = userService.deleteUser(user.getUserEmail());
        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }



    //프로필 사진 관련

    @ApiOperation(value = "로그인 유저 프로필 사진 등록/수정")
    @ApiResponses(value={
            @ApiResponse(code = 201,
                    message = "USER_PROFILE_PHOTO_ADDED/UPDATED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 400,
                    message = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/photo")
    public ResponseEntity<String> updateUserProfilePhoto(
            @RequestParam MultipartFile file
    ) throws IOException {
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        String url = userService.savePhoto(file, user);


        return new ResponseEntity<>(user.getUserPhotoUrl(), HttpStatus.OK);
    }

    @ApiOperation("유저 프로필 사진 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_PROFILE_PHOTO_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @GetMapping("/photo/{userEmail}")
    public ResponseEntity<String> getUserProfilePhoto(
            @PathVariable String userEmail
    ) {
        User user = userService.findUserByUserEmail(userEmail);
        return new ResponseEntity<>(user.getUserPhotoUrl(), HttpStatus.OK);
    }

    @ApiOperation("로그인 유저 프로필 사진 삭제")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_PROFILE_PHOTO_DELETED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @DeleteMapping("/photo")
    public ResponseEntity<String> deleteUserProfilePhoto() {
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        userService.deleteUserProfilePhoto(user);

        return new ResponseEntity<>("프로필 사진 삭제", HttpStatus.OK);
    }













    @ApiOperation("DB 에서 유저 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody UserValidateDto dto) {
        return ResponseEntity.ok().body(userService.validate(dto));
    }

    @ApiOperation("토큰 재발행")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "TOKEN_PROVIDED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public ResponseEntity<String> reIssue(@RequestBody ReIssueDto dto) {
        TokenResponseDto tokenResponseDto = userService.reissue(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokenResponseDto.getAccessToken());

        return ResponseEntity.ok().headers(headers).body("재발행 성공");
    }
}
