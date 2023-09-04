package com.project.boongobbang.domain.entity.room;

import com.project.boongobbang.util.TimeStamped;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
public class Room extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    // 매물거래구분명
    //ex) 월세, 전세
    @Column(name = "trade_type_name")
    private String tradeTypeName;

    // 전세가
    //ex) 23000
    @Column(name = "lease_price")
    private Double leasePrice;

    // 월세가
    //ex) 60
    @Column(name = "monthly_rent")
    private Double monthlyRent;

    // 월세보증금
    //ex) 1000
    @Column(name = "monthly_rent_deposit")
    private Double monthlyRentDeposit;


    // 시군구주소
    //ex) 마포구 연남동
    @Column(name = "city_address")
    private String cityAddress;

    // 읍면동명
    //ex) 연남동
    @Column(name = "town_name")
    private String townName;

    // 상세번지내용
    //ex) 566-36
    @Column(name = "detailed_address")
    private String detailedAddress;

    // 법정동코드
    //ex) 1144012400
    @Column(name = "legal_district_code")
    private String legalDistrictCode;


    // 입주가능일내용
    //ex) 20230916, NOW
    @Column(name = "move_in_date")
    private LocalDate moveInDate; // You may need to import the Date type.

    // 전용면적
    //ex) 35
    @Column(name = "exclusive_area")
    private Double exclusiveArea;

    // 공급면적
    //ex) 40
    @Column(name = "supply_area")
    private Double supplyArea;

    // 해당층구분
    //ex) 1
    @Column(name = "floor_division")
    private String floorDivision;

    // 승강기유무
    // 0 or 1로 입력받아 false or true
    @Column(name = "has_elevator")
    private Boolean hasElevator;

    // 욕실수
    @Column(name = "number_of_bathrooms")
    private Integer numberOfBathrooms;

    // 방수
    @Column(name = "number_of_rooms")
    private Integer numberOfRooms;

    // 매물종별그룹명
    //ex) 주택, 오피스텔, ..
    @Column(name = "property_group_name")
    private String propertyGroupName;

    // 카테고리2
    //ex) 다가구, 일반원룸, 빌라, ...
    @Column(name = "category_two")
    private String categoryTwo;


    // 중개업소주소
    //ex) 서울 마포구 동교동201-6  1층
    @Column(name = "brokerage_address")
    private String brokerageAddress;

    // 중개업소대표자명
    //ex) 서종만
    @Column(name = "brokerage_representative_name")
    private String brokerageRepresentativeName;

    // 중개업소명
    //ex) 에이스공인중개사사무소
    @Column(name = "brokerage_name")
    private String brokerageName;

    // 중개업소대표자휴대폰번호
    //ex) 010-7510-7007
    @Column(name = "brokerage_representative_phone")
    private String brokerageRepresentativePhone;


    // 특징광고내용
    //ex) 홍대역세권 빠른입주가능 수리된 넓은 풀옵션 원룸
    @Column(name = "feature_advertisement_content")
    private String featureAdvertisementContent;

    //방 사진
    @Column(name = "room_photos")
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomPhoto> roomPhotos;
}
