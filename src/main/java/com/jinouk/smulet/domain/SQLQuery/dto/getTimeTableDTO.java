package com.jinouk.smulet.domain.SQLQuery.dto;

import com.jinouk.smulet.domain.SQLQuery.entity.course;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    private Integer BsmOrDesign;
    private Integer SimOrSeon;
}
