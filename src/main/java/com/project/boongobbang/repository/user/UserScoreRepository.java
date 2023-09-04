package com.project.boongobbang.repository.user;

import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.domain.entity.user.UserScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Integer> {

    //ratedUser 의 점수 총 합계 반환
    @Query("SELECT SUM(us.score) FROM UserScore us WHERE us.ratedUser.userEmail = :ratedUserEmail")
    BigDecimal sumScoresByRatedUserId(@Param("ratedUserEmail") String ratedUserEmail);

    //ratingUser 의 과거 평가들 조회
    List<UserScore> findUserScoresByRatingUserOrderByCreatedAt(User ratingUser);
    Page<UserScore> findUserScoresByRatingUserOrderByCreatedAt(User ratingUser, Pageable pageable);

    //userId 와 roommateId 일치하고 평가받지 않은 UserScore 반환
    @Query("FROM UserScore us WHERE (us.ratingUser.userEmail = :userEmail AND us.ratedUser.userEmail = :roommateEmail AND us.score = -1)")
    List<UserScore> findUserScore(@Param("userEmail") String userEmail, @Param("roommateEmail") String roomateEmail);

    UserScore findUserScoreByUserScoreId(Long userScoreId);
}
