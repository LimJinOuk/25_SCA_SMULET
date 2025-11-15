package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.repository.ScheduleCourseRow;
import com.jinouk.smulet.domain.SQLQuery.repository.getTS2TE;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class getCourseTS2TE {
    private final getTS2TE repository;

    public List<ScheduleCourseRow> getTimesByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()){
            System.out.println("ids is null or empty");
            return Collections.emptyList();
        }
        System.out.println("aaa" + repository.findScheduleWithCourseByIds(ids));
        return repository.findScheduleWithCourseByIds(ids);
    }
}
