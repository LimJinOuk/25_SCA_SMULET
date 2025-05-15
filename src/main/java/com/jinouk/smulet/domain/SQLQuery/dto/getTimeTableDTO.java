package com.jinouk.smulet.domain.SQLQuery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class getTimeTableDTO {

    private String userName;
    private int timeTableId;
    private boolean majorOrCulture;
    private String classroom;
    private String courseName;
    private int credit;
    private String identifyNumberOfCourse;
    private int scheduleDay;
    private int timeStart;
    private int timeEnd;
    private String professorName;
}
