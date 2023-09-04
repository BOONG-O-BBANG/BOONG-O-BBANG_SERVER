package com.project.boongobbang.repository.user;

import com.project.boongobbang.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserNaverId(String userNaverId);
    Boolean existsByUserNaverId(String userNaverId);
    void deleteUserByUserEmail(String userEmail);
}
