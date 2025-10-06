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
        timetableCourseRepository.deleteByTimetableId(timetableId);

        for(int courseId : courseIds){
            int rows = timetableCourseRepository.settimetableCourse(timetableId , courseId);
            if(rows < 0){return false;}
        }
        return true;
    }
}
