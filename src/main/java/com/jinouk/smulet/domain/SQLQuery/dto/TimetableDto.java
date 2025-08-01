package com.jinouk.smulet.domain.SQLQuery.dto;

import com.jinouk.smulet.domain.SQLQuery.entity.timetable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableDto {
    private int timetableId;
    private int userId;
    private int term;
    private boolean tag;

    public static TimetableDto entityToDto (timetable timetable) {
        TimetableDto timetableDto = new TimetableDto();
        timetableDto.setTimetableId(timetable.getId());
        timetableDto.setUserId(timetable.getUserId().getId());
        timetableDto.setTerm(timetable.getTerm());
        timetableDto.setTag(timetable.isTag());
        return timetableDto;
    }
}
