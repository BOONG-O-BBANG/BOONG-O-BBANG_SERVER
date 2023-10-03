package com.project.boongobbang.repository.user;

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

    // code1 과 code2 를 userType 로 갖는 UserTypeFavor 객체 반환
    @Query("SELECT u " +
            "FROM UserTypeFavor u " +
            "WHERE (u.userType1 = :code1 AND u.userType2 = :code2) "
            + "OR (u.userType1 = :code2 AND u.userType2 = :code1)")
    Optional<UserTypeFavor> findByUserTypeCodes(@Param("code1") UserType code1, @Param("code2") UserType code2);


    @Query("SELECT CASE " +
            "WHEN utf.userType1 = :userType " +
            "THEN utf.userType2 " +
            "ELSE utf.userType1 " +
            "END " +
            "FROM UserTypeFavor utf " +
            "WHERE utf.userType1 = :userType " +
            "OR utf.userType2 = :userType " +
            "ORDER BY utf.count DESC")
    List<UserType> findFavoredUserTypesByUserType(@Param("userType") UserType userType);
}
