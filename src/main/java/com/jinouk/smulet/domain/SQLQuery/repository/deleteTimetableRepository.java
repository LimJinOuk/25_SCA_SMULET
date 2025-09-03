package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.entity.timetable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface deleteTimetableRepository extends JpaRepository<timetable, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM timetable WHERE id = :timetableId", nativeQuery = true)
    int deletetimetable(@Param("timetableId") Integer timetableId);
}
