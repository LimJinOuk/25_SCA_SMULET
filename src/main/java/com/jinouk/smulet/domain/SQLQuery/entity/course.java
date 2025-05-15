package com.jinouk.smulet.domain.SQLQuery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "course")
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
    private String professor_name;

    @Column
    private boolean major_or_culture;

    @Column
    private String classroom;

    @ManyToMany
    @JoinTable(
            name = "coursetoprofessor",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private List<professor> professors = new ArrayList<>();



    @ManyToOne
    @JoinColumn(name = "schedule")
    private scheduleOfCourse scheduleOfCourse;

    @ManyToMany(mappedBy = "courses")
    private List<timetable> timetables = new ArrayList<>();


}
