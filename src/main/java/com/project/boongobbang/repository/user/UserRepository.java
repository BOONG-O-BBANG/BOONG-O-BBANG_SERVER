package com.project.boongobbang.repository.user;

import com.project.boongobbang.domain.dto.user.UserProfileDto;
import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.enums.Gender;
import com.project.boongobbang.enums.SeoulGu;
import com.project.boongobbang.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserNaverId(String userNaverId);
    Boolean existsByUserNaverId(String userNaverId);
    Optional<User> findUserByUserEmail(String userEmail);
    Optional<User> findUserByUserNaverId(String userNaverId);
    void deleteUserByUserEmail(String userEmail);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT new com.project.boongobbang.domain.dto.user.UserProfileDto(u.userPhotoUrl, u.userEmail, u.username, u.userMBTI, u.userBirth, u.userLocation, u.userCleanCount, u.userHasPet, u.userHasExperience, u.userIsSmoker, u.userIsNocturnal, u.userIntroduction, u.averageScore, u.userMobile) " +
            "FROM User u " +
            "WHERE u.userType IN :userTypes " +
            "AND u.userLocation = :location " +
            "AND u.userGender = :gender " +
            "AND ABS(YEAR(u.userBirth) - YEAR(:birth)) <= 4" +
            "AND u.isPaired = false")
    List<UserProfileDto> findUserProfileDtosByPriority1(@Param("userTypes") List<UserType> userTypes, @Param("location") SeoulGu location, @Param("gender") Gender gender, @Param("birth") LocalDate birth);

    @Query("SELECT new com.project.boongobbang.domain.dto.user.UserProfileDto(u.userPhotoUrl, u.userEmail, u.username, u.userMBTI, u.userBirth, u.userLocation, u.userCleanCount, u.userHasPet, u.userHasExperience, u.userIsSmoker, u.userIsNocturnal, u.userIntroduction, u.averageScore, u.userMobile) " +
            "FROM User u " +
            "WHERE u.userLocation = :location " +
            "AND u.userGender = :gender " +
            "AND u.userEmail NOT IN :excludedEmails AND u.isPaired = false")
    List<UserProfileDto> findUserProfileDtosByPriority2(@Param("location") SeoulGu location, @Param("gender") Gender gender, @Param("excludedEmails") List<String> excludedEmails);

    @Query("SELECT new com.project.boongobbang.domain.dto.user.UserProfileDto(u.userPhotoUrl, u.userEmail, u.username, u.userMBTI, u.userBirth, u.userLocation, u.userCleanCount, u.userHasPet, u.userHasExperience, u.userIsSmoker, u.userIsNocturnal, u.userIntroduction, u.averageScore, u.userMobile) " +
            "FROM User u " +
            "WHERE u.userGender = :gender AND u.userEmail NOT IN :excludedEmails AND u.isPaired = false")
    List<UserProfileDto> findUserProfileDtosByPriority3(@Param("gender") Gender gender, @Param("excludedEmails") List<String> excludedEmails);

    @Query("SELECT new com.project.boongobbang.domain.dto.user.UserProfileDto(u.userPhotoUrl, u.userEmail, u.username, u.userMBTI, u.userBirth, u.userLocation, u.userCleanCount, u.userHasPet, u.userHasExperience, u.userIsSmoker, u.userIsNocturnal, u.userIntroduction, u.averageScore, u.userMobile) " +
            "FROM User u " +
            "WHERE u.userEmail NOT IN :excludedEmails AND u.isPaired = false")
    List<UserProfileDto> findUserProfileDtosByPriority4(@Param("excludedEmails") List<String> excludedEmails);
}
