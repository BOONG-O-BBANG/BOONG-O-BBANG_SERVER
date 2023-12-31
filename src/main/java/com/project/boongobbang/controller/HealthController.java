package com.project.boongobbang.controller;

import com.project.boongobbang.domain.dto.user.UserResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = "헬스 체크")
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://jenkins-deploy-client.s3-website.ap-northeast-2.amazonaws.com")
public class HealthController {
    @ApiOperation("Health Check")
    @ApiResponses(value={
            @ApiResponse(code = 200,
                    message = "HEALTH_CHECKED",
                    response = UserResponseDto.class),
            @ApiResponse(code = 401,
                    message = "UNAUTHORIZED_USER"),
            @ApiResponse(code = 404,
                    message = "USER_NOT_FOUND"),
            @ApiResponse(code = 500,
                    message = "SERVER_ERROR")
    })
    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }
}
