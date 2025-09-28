package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.timetableCourse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface timetableCourseRepository extends JpaRepository<timetableCourse, Long> {

    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT IGNORE INTO smulet.timetableCourse (timetable_id, course_id) 
            VALUES (:timetableId, :courseId)
            """, nativeQuery = true
    )
    public int settimetableCourse(@Param("timetableId") int timetableId, @Param("courseId") int courseId);
}
