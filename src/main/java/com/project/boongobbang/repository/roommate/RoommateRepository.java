package com.project.boongobbang.repository.roommate;

import com.project.boongobbang.domain.entity.roommate.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoommateRepository extends JpaRepository<Roommate, Long> {
    Roommate findRoommateByRoommateId(Long roommateId);

    @Query("FROM Roommate r WHERE (r.user1.userEmail = :userEmail1 AND r.user2.userEmail = :userEmail2) OR (r.user1.userEmail = :userEmail2 AND r.user2.userEmail = :userEmail1)")
    Roommate findRoommateByUsers(@Param("userEmail1") String userEmail1, @Param("userEmail2") String userEmail2);

    @Query("FROM Roommate r WHERE (r.user1.userEmail) = :userEmail OR (r.user2.userEmail) = :userEmail AND (r.endDate) IS NULL")
    Optional<Roommate> findRoommateByLoginUser(@Param("userEmail") String userEmail);

    @Query("FROM Roommate r WHERE (r.user1.userEmail = :userEmail) OR (r.user2.userEmail = :userEmail)")
    List<Roommate> findRoommatesByUserEmail(@Param("userEmail") String userEmail);

    @Modifying
    @Query("DELETE FROM Roommate r WHERE r.user1.userEmail = :userEmail OR r.user2.userEmail = :userEmail")
    void deleteRoommatesByUserEmail(@Param("userEmail") String userEmail);

}
