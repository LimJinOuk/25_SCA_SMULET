package com.jinouk.smulet.domain.SQLQuery.repository;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.entity.timetable;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@SqlResultSetMapping(
        name = "CourseDTO",
        classes = @ConstructorResult(
                targetClass = getTimeTableDTO.class,
                columns = {
                        @ColumnResult(name = "UserName", type = String.class),
                        @ColumnResult(name = "TimeTableId", type = int.class),
                        @ColumnResult(name = "전공/교양구분", type = boolean.class),
                        @ColumnResult(name = "수업명", type = String.class),  // course.name
                        @ColumnResult(name = "학점", type = int.class),
                        @ColumnResult(name = "학수번호", type = String.class),
                        @ColumnResult(name = "수업요일", type = int.class),  // scheduleOfCourse.day
                        @ColumnResult(name = "시작시각", type = int.class),
                        @ColumnResult(name = "종료시각", type = int.class),
                        @ColumnResult(name = "교수명", type = String.class)
                }
        )
)
public interface gettimeTableRepository extends JpaRepository<timetable, Long> {

    @Query(value = "SELECT DISTINCT user.name AS UserName, timetable.id AS TimeTableId, course.major_or_culture, "
            + "course.name, course.credit, course.identify_number_of_course AS 학수번호, "
            + "scheduleOfCourse.day, scheduleOfCourse.timeStart, scheduleOfCourse.timeEnd, "
            + "professor.name AS ProfessorName "
            + "FROM user "
            + "JOIN timetable ON user.id = timetable.userId "
            + "JOIN timetableCourse ON timetable.id = timetableCourse.timetableId "
            + "JOIN course ON timetableCourse.courseId = course.id "
            + "JOIN scheduleOfCourse ON course.schedule = scheduleOfCourse.id "
            + "JOIN professor ON course.professorName = professor.name "
            + "WHERE user.id = :userId", nativeQuery = true)
    List<getTimeTableDTO> findCoursesByUserId(@Param("userId") int userId);
}
