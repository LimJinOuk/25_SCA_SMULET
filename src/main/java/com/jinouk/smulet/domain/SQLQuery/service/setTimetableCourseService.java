package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.repository.timetableCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class setTimetableCourseService {

    private final timetableCourseRepository timetableCourseRepository;

    public boolean settimetableCourse(int timetableId , int courseId){
        int rows = timetableCourseRepository.settimetableCourse(timetableId , courseId);
        return rows > 0;
    }
}
