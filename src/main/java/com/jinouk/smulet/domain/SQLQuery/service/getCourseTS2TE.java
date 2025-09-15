package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.entity.schedule_of_course;
import com.jinouk.smulet.domain.SQLQuery.repository.getTS2TE;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class getCourseTS2TE {
    private final getTS2TE repository;

    public List<schedule_of_course> getTimesByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()){
            System.out.println("ids is null or empty");
            return Collections.emptyList();
        }
        return repository.findTimesByIds(ids);
    }
}
