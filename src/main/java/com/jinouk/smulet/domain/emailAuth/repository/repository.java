package com.jinouk.smulet.domain.emailAuth.repository;

import com.jinouk.smulet.domain.emailAuth.entity.entity;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface repository extends JpaRepository<entity, Long> {
    Optional<entity> findByEmail(String email);

    void deleteByEmail(String email);
}
