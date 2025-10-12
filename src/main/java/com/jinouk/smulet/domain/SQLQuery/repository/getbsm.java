package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface getbsm extends JpaRepository<course, Long> {
    @Query(value = """
        SELECT COUNT(DISTINCT c.`identify_number_of_course`)
        FROM `timetable` t
        JOIN `timetableCourse` tc ON tc.`timetable_id` = t.`id`
        JOIN `course` c ON c.`id` = tc.`course_id`
        WHERE t.`user_id` = :userId
          AND c.`bsm또는설계` = 0
        """, nativeQuery = true)
    int findBsmZeroListByUserId(@Param("userId") int userId);

    @Query(value = """
        SELECT COUNT(DISTINCT c.`identify_number_of_course`)
        FROM `timetable` t
        JOIN `timetableCourse` tc ON tc.`timetable_id` = t.`id`
        JOIN `course` c ON c.`id` = tc.`course_id`
        WHERE t.`user_id` = :userId
          AND c.`bsm또는설계` = 1
        """, nativeQuery = true)
    int findBsmOneListByUserId(@Param("userId") int userId);

}
