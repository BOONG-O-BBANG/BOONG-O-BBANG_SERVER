package com.project.boongobbang.controller;

import com.project.boongobbang.domain.dto.user.UserResponseDto;
import com.project.boongobbang.domain.entity.roommate.Notification;
import com.project.boongobbang.domain.entity.roommate.Roommate;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.NotificationType;
import com.project.boongobbang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "룸메이트 API")
@RestController
@RequestMapping("/roommates")
@RequiredArgsConstructor

public class RoommateController {
    private final UserService userService;

    @ApiOperation("룸메이트 신청")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "ROOMMATE_REQUEST_TRANSMITED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/request/{receiverEmail}")
    public ResponseEntity<String> sendRoommateRequest(@PathVariable String receiverEmail){
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);
        String senderUserEmail = user.getUserEmail();

        if(userService.validateIsPaired(senderUserEmail, receiverEmail)){
            return new ResponseEntity<>("이미 룸메이트가 있는 유저입니다", HttpStatus.BAD_REQUEST);
        }
        if(userService.validateIsExistingNotification(senderUserEmail, receiverEmail)){
            return new ResponseEntity<>("이미 요청을 보냈습니다", HttpStatus.BAD_REQUEST);
        }
        Notification notification = userService.sendRoommateRequest(senderUserEmail, receiverEmail);
        return new ResponseEntity<>("룸메이트 신청이 완료되었습니다\nnotificationId : " + notification.getNotificationId(), HttpStatus.OK);
    }

    @ApiOperation("룸메이트 신청 수락")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "ROOMMATE_REQUEST_ACCEPTED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/accept/{notificationId}")
    public ResponseEntity<String> acceptRoommateRequest(@PathVariable Long notificationId){
        Notification notification = userService.findNotificationByNotificationId(notificationId);
        if(notification.getNotificationType() != NotificationType.REQUEST){
            return new ResponseEntity<>("수락할 수 있는 요청이 아닙니다", HttpStatus.BAD_REQUEST);
        }
        if(userService.validateIsPaired2(notification)){
            return new ResponseEntity<>("이미 룸메이트가 있는 유저입니다", HttpStatus.BAD_REQUEST);
        }

        Roommate roommate = userService.acceptRoommateRequest(notificationId);
        return new ResponseEntity<>("룸메이트 신청을 수락했습니다\nroommateId : " + roommate.getRoommateId(), HttpStatus.OK);
    }

    @ApiOperation("룸메이트 신청 거절")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "ROOMMATE_REQUEST_REJECTED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/request/{notificationId}")
    public ResponseEntity<String> deleteRequest(@PathVariable Long notificationId){
        userService.deleteNotification(notificationId);
        return new ResponseEntity<>("룸메이트 신청을 거절했습니다", HttpStatus.OK);
    }
}