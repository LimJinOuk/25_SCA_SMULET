package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.scheduleOfCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface getTS2TE extends JpaRepository<scheduleOfCourse, Integer> {

    @Query(value = """
    SELECT
        soc.id   AS scheduleId,
        soc.day  AS day,
        soc.time_start AS timeStart,
        soc.time_end   AS timeEnd,
        c.id     AS courseId,
        c.name   AS courseName,
        c.`bsm또는설계` As bsm,
        c.`전심전선` AS 전선전심,
        c.credit AS credit,
        c.professor_name AS professorName,
        c.identify_number_of_course AS identifyNumberOfCourse,
        c.classroom AS classroom
    FROM schedule_of_course soc
    LEFT JOIN course c ON c.schedule = soc.id
    WHERE soc.id IN (:scheduleIds)
    ORDER BY soc.id, c.id
    """, nativeQuery = true)
    List<ScheduleCourseRow> findScheduleWithCourseByIds(@Param("scheduleIds") List<Integer> scheduleIds);

}
