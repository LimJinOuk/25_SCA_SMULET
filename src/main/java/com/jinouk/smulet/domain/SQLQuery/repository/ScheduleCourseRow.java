package com.jinouk.smulet.domain.SQLQuery.repository;

public interface ScheduleCourseRow {
    Integer getScheduleId();
    Integer getDay();
    Integer getTimeStart();
    Integer getTimeEnd();

    Integer getCourseId();
    String  getCourseName();
    String  getProfessorName();
    String  getIdentifyNumberOfCourse();
    String  getClassroom();
}
