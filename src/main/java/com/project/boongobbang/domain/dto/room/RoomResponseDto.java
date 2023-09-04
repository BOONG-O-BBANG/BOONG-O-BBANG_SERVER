package com.project.boongobbang.domain.dto.room;

import com.project.boongobbang.domain.entity.room.Room;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class RoomResponseDto {
    private Long roomId;

    // 매물거래구분명
    private String tradeTypeName;

    // 전세가
    private Double leasePrice;

    // 월세가
    private Double monthlyRent;

    // 월세보증금
    private Double monthlyRentDeposit;

    // 시군구주소
    private String cityAddress;

    // 읍면동명
    private String townName;

    // 상세번지내용
    private String detailedAddress;

    // 법정동코드
    private String legalDistrictCode;

    // 입주가능일내용
    private LocalDate moveInDate; // You may need to import the Date type.

    // 전용면적
    private Double exclusiveArea;

    // 공급면적
    private Double supplyArea;

    // 해당층구분
    private String floorDivision;

    // 승강기유무
    private Boolean hasElevator;

    // 욕실수
    private Integer numberOfBathrooms;

    // 방수
    private Integer numberOfRooms;

    // 매물종별그룹명
    private String propertyGroupName;

    // 카테고리2
    private String categoryTwo;

    // 중개업소주소
    private String brokerageAddress;

    // 중개업소대표자명
    private String brokerageRepresentativeName;

    // 중개업소명
    private String brokerageName;

    // 중개업소대표자휴대폰번호
    private String brokerageRepresentativePhone;

    // 특징광고내용
    private String featureAdvertisementContent;

    public RoomResponseDto(Room room) {
        this.roomId = room.getRoomId();
        this.tradeTypeName = room.getTradeTypeName();
        this.leasePrice = room.getLeasePrice();
        this.monthlyRent = room.getMonthlyRent();
        this.monthlyRentDeposit = room.getMonthlyRentDeposit();
        this.cityAddress = room.getCityAddress();
        this.townName = room.getTownName();
        this.detailedAddress = room.getDetailedAddress();
        this.legalDistrictCode = room.getLegalDistrictCode();
        this.moveInDate = room.getMoveInDate();
        this.exclusiveArea = room.getExclusiveArea();
        this.supplyArea = room.getSupplyArea();
        this.floorDivision = room.getFloorDivision();
        this.hasElevator = room.getHasElevator();
        this.numberOfBathrooms = room.getNumberOfBathrooms();
        this.numberOfRooms = room.getNumberOfRooms();
        this.propertyGroupName = room.getPropertyGroupName();
        this.categoryTwo = room.getCategoryTwo();
        this.brokerageAddress = room.getBrokerageAddress();
        this.brokerageRepresentativeName = room.getBrokerageRepresentativeName();
        this.brokerageName = room.getBrokerageName();
        this.brokerageRepresentativePhone = room.getBrokerageRepresentativePhone();
        this.featureAdvertisementContent = room.getFeatureAdvertisementContent();
    }
}

