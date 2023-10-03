package com.project.boongobbang.util;

import com.project.boongobbang.enums.UserType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_type_favor")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserTypeFavor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_type1")
    @Enumerated(EnumType.STRING)
    private UserType userType1;

    @Column(name = "user_type2")
    @Enumerated(EnumType.STRING)
    private UserType userType2;

    @Column(name = "count")
    private int count;
    public void setCount(int count){
        this.count = count;
    }
}