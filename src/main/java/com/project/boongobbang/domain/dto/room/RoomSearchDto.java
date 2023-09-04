package com.project.boongobbang.domain.dto.room;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomSearchDto {
    // 매물거래구분명
    //ex) 월세, 전세
    private String tradeTypeName;

    // 전세가
    private Double leasePrice;

    // 월세가
    private Double monthlyRent;

    // 월세보증금
    private Double monthlyRentDeposit;

    // 시군구주소
    //ex) 마포구 연남동
    private String cityAddress;

    // 입주가능일내용
    //ex) 20230916, NOW
    private LocalDate moveInDate; // You may need to import the Date type.
}

