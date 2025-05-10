package com.jinouk.smulet.domain.SQLQuery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@Table(name = "schedule_of_course")
public class scheduleOfCourse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int day;

    @Column
    private int time_start;

    @Column
    private int time_end;

    @OneToMany(mappedBy = "scheduleOfCourse")
    private List<course> courses = new ArrayList<>();
}
