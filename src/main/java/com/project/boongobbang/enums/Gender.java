package com.project.boongobbang.enums;


import com.project.boongobbang.exception.AppException;
import com.project.boongobbang.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {
    MAN('M'),
    WOMAN('W');

    private char type;

    public static Gender of(char type) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.getType() == type)
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_GENDER, "찾으시는 성별이 없습니다."));
    }
}
