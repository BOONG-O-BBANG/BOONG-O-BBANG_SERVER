package com.project.boongobbang.enums;

import com.project.boongobbang.exception.AppException;
import com.project.boongobbang.exception.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CleanCount {
    ONE_TO_TWO(0),
    THREE_TO_FOUR(1),
    MORE_THAN_FOUR(2);

    private int type;

    CleanCount(int type){
        this.type = type;
    }

    public static CleanCount of(int type){
        return Arrays.stream(CleanCount.values())
                .filter(cleanCount -> cleanCount.getType() == (type))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CLEAN_COUNT, "잘못된 청소 횟수입니다."));
    }
}
