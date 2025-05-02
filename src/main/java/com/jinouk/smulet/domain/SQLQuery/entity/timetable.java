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
public class timetable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private user user;

    @ManyToMany
    @JoinTable(
            name = "timetableCourse",
            joinColumns = @JoinColumn(name = "timetableId"),
            inverseJoinColumns = @JoinColumn(name = "courseId")
    )
    private List<course> courses = new ArrayList<>();

}
