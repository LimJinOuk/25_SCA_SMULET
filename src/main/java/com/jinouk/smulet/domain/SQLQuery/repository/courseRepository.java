package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface courseRepository extends JpaRepository<course, Long> {
    List<course> findByYear(int year);
}
