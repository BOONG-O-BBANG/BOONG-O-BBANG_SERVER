package com.project.boongobbang.repository.user;

import com.project.boongobbang.domain.entity.user.User;
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
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserNaverId(String userNaverId);
    Boolean existsByUserNaverId(String userNaverId);
    Optional<User> findUserByUserEmail(String userEmail);
    Optional<User> findUserByUserNaverId(String userNaverId);
    void deleteUserByUserEmail(String userEmail);


    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.userType " +
                "IN :userTypes " +
                "AND u.userLocation = :location " +
                "AND u.userGender = :gender " +
                "AND ABS(YEAR(u.userBirth) - YEAR(:birth)) <= 4")
    List<User> findUsersByPriority1(@Param("userTypes") List<UserType> userTypes, @Param("location") String location, @Param("gender") String gender, @Param("birth") LocalDate birth, Pageable pageable);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.userLocation = :location AND u.userGender = :gender AND u NOT IN :excludedUsers")
    Page<User> findByUserLocationAndUserGenderExcludingUsers(String location, String gender, Set<User> excludedUsers, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.userGender = :gender AND u NOT IN :excludedUsers")
    Page<User> findByUserGenderExcludingUsers(String gender, Set<User> excludedUsers, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u NOT IN :excludedUsers")
    Page<User> findAllExcludingUsers(Set<User> excludedUsers, Pageable pageable);


}
