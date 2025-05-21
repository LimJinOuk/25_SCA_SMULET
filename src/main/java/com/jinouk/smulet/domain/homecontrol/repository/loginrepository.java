package com.jinouk.smulet.domain.homecontrol.repository;


import com.jinouk.smulet.domain.homecontrol.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface loginrepository extends JpaRepository<user, Long>
{
    Optional<user> findByEmail(String email);

    void deleteByEmail(String email);

    Optional<user> findByName(String userName);
}
