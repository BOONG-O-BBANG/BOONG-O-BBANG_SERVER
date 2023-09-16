package com.project.boongobbang.controller;

import com.project.boongobbang.domain.dto.roommate.NotificationResponseDto;
import com.project.boongobbang.domain.dto.roommate.RoommateResponseDto;
import com.project.boongobbang.domain.dto.user.UserProfileDto;
import com.project.boongobbang.domain.dto.user.UserResponseDto;
import com.project.boongobbang.domain.dto.user.UserSimpleDto;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "관리자 API")
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://jenkins-deploy-client.s3-website.ap-northeast-2.amazonaws.com")
public class AdminController {
    private final UserService userService;


    @ApiOperation("전체 알림 페이지로 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "NOTIFICATIONS_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "NOTIFICATION_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{pageNumber}")
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications(
            @PathVariable int pageNumber){
        List<NotificationResponseDto> notificationResponseDtoList = userService.getNotificationsByPage(pageNumber - 1);
        return new ResponseEntity<>(notificationResponseDtoList, HttpStatus.OK);
    }

    @ApiOperation("전체 룸메이트 리스트로 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "ROOMMATES_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "ROOMMATES_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{pageNumber}")
    public ResponseEntity<List<RoommateResponseDto>> getAllRoommates(
            @PathVariable int pageNumber
    ){
        List<RoommateResponseDto> roommateResponseDtoList = userService.getRoommateList(pageNumber);
        return new ResponseEntity<>(roommateResponseDtoList, HttpStatus.OK);
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

    @ApiOperation("유저 계정 삭제")
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
    @DeleteMapping("/{userEmail}")
    public ResponseEntity<UserProfileDto> deleteUser(
            @PathVariable String userEmail
    ) {
        User user = userService.findUserByUserEmail(userEmail);
        UserProfileDto userProfileDto = userService.deleteUser(user.getUserEmail());
        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }
}
