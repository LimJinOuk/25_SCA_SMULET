package com.jinouk.smulet.domain.SQLQuery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int credit;

    @Column
    private String name;

    @Column
    private String identify_number_of_course;

    @Column
    private String professorName;
    
    @ManyToMany
    @JoinTable(
            name = "courseToProfessor",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "professorId")
    )
    private List<professor> professors = new ArrayList<>();

    @Column
    private boolean major_or_culture;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private scheduleOfCourse scheduleOfCourse;

    @ManyToMany(mappedBy = "courses")
    private List<timetable> timetables = new ArrayList<>();


}
