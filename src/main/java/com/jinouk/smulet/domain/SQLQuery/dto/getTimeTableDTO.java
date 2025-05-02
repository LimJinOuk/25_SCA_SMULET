package com.jinouk.smulet.domain.SQLQuery.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class getTimeTableDTO {

    private String userName;
    private int timeTableId;
    private boolean majorOrCulture;
    private String courseName;
    private int credit;
    private String identifyNumberOfCourse;
    private int scheduleDay;
    private int timeStart;
    private int timeEnd;
    private String professorName;
}
