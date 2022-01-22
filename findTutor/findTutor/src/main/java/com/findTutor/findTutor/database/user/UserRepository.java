package com.findTutor.findTutor.database.user;

import com.findTutor.findTutor.database.user.model.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// this interface is responsible for data access
// we use this interface inside service
// tutor- type we want this repository to work on   Long- type of id in Tutor class
@Repository
public interface UserRepository extends JpaRepository<DBUser, Long> {

    @Query("SELECT t FROM DBUser t WHERE t.email =?1")
    Optional<DBUser> findUserByEmail(String email);

    Optional<DBUser> findByEmailAndPassword(String email, String password);
}
