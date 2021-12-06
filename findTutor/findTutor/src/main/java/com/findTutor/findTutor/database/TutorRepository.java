package com.findTutor.findTutor.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// this interface is responsible for data access
// we use this interface inside service
// tutor- type we want this repository to work on   Long- type of id in Tutor class
@Repository
public interface TutorRepository extends JpaRepository <Tutor, Long> {

    @Query("SELECT t FROM Tutor t WHERE t.email =?1")
    Optional<Tutor> findTutorByEmail(String email);
}
