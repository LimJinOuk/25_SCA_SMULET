package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.repository.timetableCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class setTimetableCourseService {

    private final timetableCourseRepository timetableCourseRepository;

    public boolean settimetableCourse(int timetableId , List<Integer> courseIds){
        int inserted = 0;
        int skipped = 0;

        for(int courseId : courseIds){
            int rows = timetableCourseRepository.settimetableCourse(timetableId , courseId);
            if(rows == 1) inserted++;
            else skipped++;
            if(rows < 0){return false;}
        }
        return true;
    }
}
