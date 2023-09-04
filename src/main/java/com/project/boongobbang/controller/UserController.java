package com.project.boongobbang.controller;


import com.project.boongobbang.domain.dto.user.UserSignUpDto;
import com.project.boongobbang.domain.dto.user.UserValidateDto;
import com.project.boongobbang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody UserValidateDto dto) {
        return ResponseEntity.ok().body(userService.validate(dto));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpDto dto) {
        userService.signUp(dto);

        return ResponseEntity.ok().body("회원가입이 되었습니다.");
    }
}
