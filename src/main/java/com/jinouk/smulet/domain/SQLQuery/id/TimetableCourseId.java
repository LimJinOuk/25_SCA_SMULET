package com.jinouk.smulet.domain.SQLQuery.id;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TimetableCourseId implements Serializable {

    @Column(name = "timetable_id")
    private int timetableId;

    @Column(name = "course_id")
    private int courseId;
}
