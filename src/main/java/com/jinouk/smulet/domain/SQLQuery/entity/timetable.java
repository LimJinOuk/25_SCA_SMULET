package com.jinouk.smulet.domain.SQLQuery.entity;


import com.jinouk.smulet.domain.homecontrol.entity.user;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "timetable")
public class timetable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private user userId;

    @Column
    private int term;

    @Column
    private boolean tag;

    @ManyToMany
    @JoinTable(
            name = "timetableCourse",
            joinColumns = @JoinColumn(name = "timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<course> courses = new ArrayList<>();

}
