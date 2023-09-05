package com.project.boongobbang.controller;

import com.project.boongobbang.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "알림 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor

public class NotificationController {
    private final UserService userService;
}
