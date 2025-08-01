package com.jinouk.smulet.domain.homecontrol.dto;

import com.jinouk.smulet.domain.SQLQuery.dto.TimetableDto;
import com.jinouk.smulet.domain.SQLQuery.entity.timetable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private String name;
    private String email;
    private List<TimetableDto> timetables;
}
