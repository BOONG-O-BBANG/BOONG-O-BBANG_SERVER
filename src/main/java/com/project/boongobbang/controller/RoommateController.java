package com.project.boongobbang.controller;

import com.project.boongobbang.domain.dto.roommate.HistoryResponseDto;
import com.project.boongobbang.domain.dto.user.UserProfileDto;
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

import java.util.List;

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
    public ResponseEntity<String> sendRoommateRequest(
            @PathVariable String receiverEmail){
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
    @PostMapping("/request/accept/{notificationId}")
    public ResponseEntity<String> acceptRoommateRequest(
            @PathVariable Long notificationId){
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
    public ResponseEntity<String> deleteRequest(
            @PathVariable Long notificationId){
        userService.deleteNotification(notificationId);
        return new ResponseEntity<>("룸메이트 신청을 거절했습니다", HttpStatus.OK);
    }

    @ApiOperation("로그인 유저 룸메이트 관계 종료")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "ROOMMATE_ENDED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping()
    public ResponseEntity<String> endRoommate(){
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        Roommate roommate = userService.findRoommateByLoginUser(user.getUserEmail());

        if(roommate == null){
            return new ResponseEntity<>("룸메이트가 없습니다", HttpStatus.BAD_REQUEST);
        }

        userService.endRoommate(roommate.getRoommateId());
        return new ResponseEntity<>("룸메이트 관계를 끊었습니다", HttpStatus.OK);
    }



    @ApiOperation("로그인 유저의 매칭 상태 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_ROOMMATE_STATUS_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matching")
    public ResponseEntity<List<UserProfileDto>> getUserMatchingStatus(){
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        List<UserProfileDto> userAndRoommate = userService.getUserAndRoommate(user);
        return new ResponseEntity<>(userAndRoommate, HttpStatus.OK);
    }



    @ApiOperation("로그인 유저 과거 룸메이트 목록 페이지로 조회")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_PREVIOUS_ROOMMATES_FOUND",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/history/{pageNumber}")
    public ResponseEntity<List<HistoryResponseDto>> getRoommateHistoryList(
            @PathVariable int pageNumber){
        String userNaverId = userService.getLoginUserInfo();
        User user = userService.findUserByUserNaverId(userNaverId);

        List<HistoryResponseDto> historyResponseDtoList = userService.returnUserPreviousRoommatesByPage(user, pageNumber - 1);
        return new ResponseEntity<>(historyResponseDtoList, HttpStatus.OK);
    }

    @ApiOperation("유저 과거 룸메이트 평가")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "USER_PREVIOUS_ROOMMATES_RATED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/score/{userScoreId}")
    public ResponseEntity<String> rateRoommate(
            @PathVariable Long userScoreId,
            int score){
        if(userService.validateUserScoreRated(userScoreId)){
            return new ResponseEntity<>("이미 평가된 기록입니다", HttpStatus.BAD_REQUEST);
        }
        userService.rateRoommate(userScoreId, score);
        return new ResponseEntity<>("룸메이트 평가 완료", HttpStatus.OK);
    }

}
