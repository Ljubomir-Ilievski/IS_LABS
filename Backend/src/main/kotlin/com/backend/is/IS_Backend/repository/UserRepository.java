package com.backend.is.IS_Backend.repository;

import mk.ukim.finki.it.reservengo.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByEnabledFalseAndLastActivityDateBefore(LocalDateTime time);
    List<User> findAllByEnabledTrueAndLastActivityDateBefore(LocalDateTime time);
}

