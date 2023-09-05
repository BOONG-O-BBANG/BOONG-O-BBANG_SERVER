package com.project.boongobbang.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, ""),
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ""),
    INVALID_CLEAN_COUNT(HttpStatus.NOT_FOUND, ""),
    INVALID_GENDER(HttpStatus.NOT_FOUND, ""),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, ""),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "");

    private final HttpStatus httpStatus;
    private final String message;

}

