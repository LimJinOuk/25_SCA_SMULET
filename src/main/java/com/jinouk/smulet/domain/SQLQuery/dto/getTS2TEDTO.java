package com.jinouk.smulet.domain.SQLQuery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
public class getTS2TEDTO {
    private List<Integer> getScheduleIds;
}
