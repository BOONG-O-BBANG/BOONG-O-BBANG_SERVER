package com.project.boongobbang.repository.user;

import com.project.boongobbang.domain.entity.user.User;
import com.project.boongobbang.domain.entity.user.UserScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, Integer> {

    //ratedUser 의 점수 총 합계 반환
    @Query("SELECT SUM(us.score) FROM UserScore us WHERE us.ratedUser.userEmail = :ratedUserEmail")
    BigDecimal sumScoresByRatedUserEmail(@Param("ratedUserEmail") String ratedUserEmail);

    //ratingUser 의 과거 평가들 조회
    List<UserScore> findUserScoresByRatingUserOrderByCreatedAt(User ratingUser);
    Page<UserScore> findUserScoresByRatingUserOrderByCreatedAt(User ratingUser, Pageable pageable);

    //userId 와 roommateId 가 일치하고 점수 -1인(아직 평가 안한) UserScore 반환
    @Query("FROM UserScore us WHERE (us.ratingUser.userEmail = :userEmail AND us.ratedUser.userEmail = :roommateEmail AND us.score = -1)")
    List<UserScore> findUserScore(@Param("userEmail") String userEmail, @Param("roommateEmail") String roommateEmail);

    UserScore findUserScoreByUserScoreId(Long userScoreId);

    @Modifying
    @Query("DELETE FROM UserScore us WHERE us.ratingUser.userEmail = :userEmail OR us.ratedUser.userEmail = :userEmail")
    void deleteUserScoresByUserEmail(@Param("userEmail") String userEmail);

}
