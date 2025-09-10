package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.schedule_of_course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface getTS2TE extends JpaRepository<schedule_of_course, Integer> {

    @Query(value = """
        SELECT 
            soc.id          AS scheduleId,
            soc.time_start  AS timeStart,
            soc.time_end    AS timeEnd,
            soc.day         AS day
        FROM schedule_of_course soc
        WHERE soc.id IN (:scheduleIds)
        """, nativeQuery = true)
    List<schedule_of_course> findTimesByIds(@Param("scheduleIds") List<Integer> scheduleIds);
}
