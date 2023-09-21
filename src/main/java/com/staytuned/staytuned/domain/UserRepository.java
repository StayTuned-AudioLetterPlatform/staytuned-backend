package com.staytuned.staytuned.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCode(Long code);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}

