package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.timetableCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface getTimetableRepo extends JpaRepository<timetableCourse, Long> {
    public interface TimetableCourseRow {
        Integer getTimetableId();
        Integer getCourseId();
    }

    @Query(value = """
        SELECT 
            tc.timetable_id AS timetableId,
            tc.course_id    AS courseId
        FROM timetableCourse tc
        WHERE tc.timetable_id = :timetableId
        """, nativeQuery = true)
    List<TimetableCourseRow> findAllByTimetableId(@Param("timetableId") Integer timetableId);
}
