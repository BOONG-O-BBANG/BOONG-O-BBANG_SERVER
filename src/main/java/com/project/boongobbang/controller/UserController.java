package com.project.boongobbang.controller;


import com.project.boongobbang.domain.dto.token.TokenResponseDto;
import com.project.boongobbang.domain.dto.user.*;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody UserValidateDto dto) {
        return ResponseEntity.ok().body(userService.validate(dto));
    }

    @ApiOperation("유저 회원가입")
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

        return ResponseEntity.ok().body("회원가입이 되었습니다.");
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
    public ResponseEntity<UserResponseDto> getUserDetail(
            @AuthenticationPrincipal User loginUser) {
        User user = userService.findUserByUserEmail(loginUser.getUserEmail());
        UserResponseDto userResponseDto = userService.returnUserDto(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @ApiOperation("유저 본인 프로필 조회")
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
    public ResponseEntity<UserProfileDto> getMyProfile(
            @AuthenticationPrincipal User loginUser) {
        User user = userService.findUserByUserEmail(loginUser.getUserEmail());
        UserProfileDto userProfileDto = userService.returnMyProfileDto(user);
        return new ResponseEntity<>(userProfileDto, HttpStatus.CREATED);
    }

    @ApiOperation("전체 유저 페이지로 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USERS_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USERS_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<List<UserSimpleDto>> getAllUsersByPage(
            @PathVariable int pageNumber) {
        List<UserSimpleDto> userSimpleDtoList = userService.getUsersByPage(pageNumber - 1);
        return new ResponseEntity<>(userSimpleDtoList, HttpStatus.OK);
    }

    @ApiOperation("유저 정보 수정")
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
            @AuthenticationPrincipal User loginUser,
            @RequestBody UserUpdateRequestDto dto) {
        User user = userService.updateUser(loginUser.getUserEmail(), dto);
        UserResponseDto userResponseDto = userService.returnUserDto(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @ApiOperation("유저 스스로 탈퇴 (임시)")
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
    public ResponseEntity<UserProfileDto> deleteUser(
            @AuthenticationPrincipal User loginUser) {
        UserProfileDto userProfileDto = userService.deleteUser(loginUser.getUserEmail());
        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }
}
