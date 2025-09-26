package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface getbsm extends JpaRepository<course, Long> {
    @Query(value = "SELECT bsm또는설계 FROM course WHERE bsm또는설계 = 0", nativeQuery = true)
    List<String> findBsmOrDesignEqualsZero();

    @Query(value = "SELECT bsm또는설계 FROM course WHERE bsm또는설계 = 1", nativeQuery = true)
    List<String> findBsmOrDesignEqualsOne();
}
