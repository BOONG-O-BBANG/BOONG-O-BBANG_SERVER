package com.project.boongobbang.controller;

import com.project.boongobbang.domain.dto.roommate.NotificationResponseDto;
import com.project.boongobbang.domain.dto.user.UserResponseDto;
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

@Api(tags = "알림 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final UserService userService;

    @ApiOperation("로그인 유저 알림 목록 페이지로 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_NOTIFICATIONS_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{pageNumber}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationList(
            @PathVariable int pageNumber){
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        List<NotificationResponseDto> notificationResponseDtoList = userService.getNotificationsByUserAndPage(user, pageNumber - 1);
        return new ResponseEntity<>(notificationResponseDtoList, HttpStatus.OK);
    }

    @ApiOperation("알림 삭제")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_NOTIFICATION_DELETED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long notificationId){
        userService.deleteNotification(notificationId);
        return new ResponseEntity<>("알림을 삭제했습니다", HttpStatus.OK);
    }

}
