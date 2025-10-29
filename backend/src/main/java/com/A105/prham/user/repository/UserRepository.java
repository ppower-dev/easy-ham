package com.A105.prham.user.repository;

import com.A105.prham.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userSkills us " +
            "LEFT JOIN FETCH us.skill " +
            "LEFT JOIN FETCH u.userPositions up " +
            "LEFT JOIN FETCH up.position " +
            "WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
