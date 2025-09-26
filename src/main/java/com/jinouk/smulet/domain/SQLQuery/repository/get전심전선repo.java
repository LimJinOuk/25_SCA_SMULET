package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface get전심전선repo extends JpaRepository<course, Integer> {
    @Query(value = "SELECT 전심전선 FROM course WHERE 전심전선 = 0", nativeQuery = true)
    List<String> findBsmOrDesignEqualsZero();

    @Query(value = "SELECT 전심전선 FROM course WHERE 전심전선 = 1", nativeQuery = true)
    List<String> findBsmOrDesignEqualsOne();
}
