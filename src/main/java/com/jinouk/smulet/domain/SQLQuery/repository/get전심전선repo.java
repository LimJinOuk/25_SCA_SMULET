package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface get전심전선repo extends JpaRepository<course, Integer> {
    @Query(value = """
        SELECT COUNT(DISTINCT c.`identify_number_of_course`)
        FROM `timetable` t
        JOIN `timetableCourse` tc ON tc.`timetable_id` = t.`id`
        JOIN `course` c ON c.`id` = tc.`course_id`
        WHERE t.`user_id` = :userId
          AND c.`전심전선` = 0
        """, nativeQuery = true)
    int countDesignZeroUniqueCourseByUser(@Param("userId") int userId);


    @Query(value = """
        SELECT COUNT(DISTINCT c.`identify_number_of_course`)
        FROM `timetable` t
        JOIN `timetableCourse` tc ON tc.`timetable_id` = t.`id`
        JOIN `course` c ON c.`id` = tc.`course_id`
        WHERE t.`user_id` = :userId
          AND c.`전심전선` = 1
        """, nativeQuery = true)
    int countDesignOneUniqueCourseByUser(@Param("userId") int userId);

}
