package com.jinouk.smulet.domain.SQLQuery.entity;

import com.jinouk.smulet.domain.SQLQuery.id.TimetableCourseId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
@Table(name = "timetableCourse")
public class timetableCourse {

    @EmbeddedId
    private TimetableCourseId timetableCourseIdPK;
}
