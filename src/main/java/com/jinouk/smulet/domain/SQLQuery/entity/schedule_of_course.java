package com.jinouk.smulet.domain.SQLQuery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule_of_course")
public class schedule_of_course {

    @Id
    private int id;

    @Column
    private int time_start;

    @Column
    private int time_end;
}
