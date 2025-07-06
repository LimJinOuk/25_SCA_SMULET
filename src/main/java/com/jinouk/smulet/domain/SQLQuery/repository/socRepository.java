package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.scheduleOfCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface socRepository extends JpaRepository<scheduleOfCourse, Long> {
    scheduleOfCourse findScheduleOfCourseById(int id);
}
