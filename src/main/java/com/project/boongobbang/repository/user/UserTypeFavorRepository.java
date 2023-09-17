package com.project.boongobbang.repository.user;


import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.Gender;
import com.project.boongobbang.enums.SeoulGu;
import com.project.boongobbang.enums.UserType;
import com.project.boongobbang.util.UserTypeFavor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserTypeFavorRepository extends JpaRepository<UserTypeFavor, Long> {

    // code1 과 code2 를 userTypeCode 로 갖는 UserTypeFavor 객체 반환
    @Query("SELECT u " +
            "FROM UserTypeFavor u " +
            "WHERE (u.userTypeCode1 = :code1 AND u.userTypeCode2 = :code2) "
            + "OR (u.userTypeCode1 = :code2 AND u.userTypeCode2 = :code1)")
    Optional<UserTypeFavor> findByUserTypeCodes(@Param("code1") UserType code1, @Param("code2") UserType code2);


    @Query("SELECT CASE " +
                "WHEN utf.userTypeCode1 = :userType " +
                    "THEN utf.userTypeCode2 " +
                "ELSE utf.userTypeCode1 " +
                "END " +
            "FROM UserTypeFavor utf " +
            "WHERE utf.userTypeCode1 = :userType " +
                "OR utf.userTypeCode2 = :userType " +
                "ORDER BY utf.count DESC")
    List<UserType> findFavoredUserTypesByUserType(@Param("userType") UserType userType);


}
