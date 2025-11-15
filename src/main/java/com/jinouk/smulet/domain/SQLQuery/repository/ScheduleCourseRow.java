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
    Integer getBsm();       // AS bsm
    Integer get전선전심();
    Integer getCredit();
}
