package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.entity.course;
import com.jinouk.smulet.domain.SQLQuery.entity.scheduleOfCourse;
import com.jinouk.smulet.domain.SQLQuery.entity.timetable;
import com.jinouk.smulet.domain.SQLQuery.repository.courseRepository;
import com.jinouk.smulet.domain.SQLQuery.repository.gettimeTableRepository;
import com.jinouk.smulet.domain.SQLQuery.repository.socRepository;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class getTimeTableService {
    public final gettimeTableRepository timetablerepository;
    public final courseRepository courserepository;
    public final socRepository socRepository;
    public final loginrepository loginrepository;

    public List<getTimeTableDTO> getcoursesByUserID(int userId)
    {
        return timetablerepository.findCoursesByUserId(userId);
    }

    public Map<String, List<Map<String, Object>>> find_tableIDs(Integer userId)
    {
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        List<Map<String, Object>> list = timetablerepository.findTablesByUserId(userId);
        map.put("Timetable List", list);
        return map;
    }

    public List<getTimeTableDTO> allCourse() {
        List<course> courses = courserepository.findAll();
        List<getTimeTableDTO> result = new ArrayList<>();

        for (course course : courses) {
            getTimeTableDTO dto = new getTimeTableDTO();
            dto.setTimeTableId(course.getId());
            dto.setMajorOrCulture(course.isMajor_or_culture());
            dto.setClassroom(course.getClassroom());
            dto.setCourseName(course.getName());
            dto.setCredit(course.getCredit());
            dto.setIdentifyNumberOfCourse(course.getIdentify_number_of_course());
            scheduleOfCourse schedule = socRepository.findScheduleOfCourseById(course.getScheduleOfCourse().getId());
            dto.setScheduleDay(schedule.getDay());
            dto.setTimeStart(schedule.getTime_start());
            dto.setTimeEnd(schedule.getTime_end());
            dto.setProfessorName(course.getProfessor_name());

            result.add(dto);
        }

        return result;
    }

    public void addNewTimetable(String username, int semester) {
        Optional<user> entity = loginrepository.findByName(username);
        if (entity.isPresent()) {
            timetable timetable = new timetable();
            timetable.setUserId(entity.get());
            timetable.setTerm(semester);
            timetable.setTag(false);

            timetablerepository.save(timetable);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional
    public Map<String, String> representativeTimeTable(int userId, int timetableId) {
        Map<String, String> result = new HashMap<>();

        timetable target = timetablerepository.findByIdAndUserId_Id(timetableId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 시간표가 아닙니다."));

        if(target.isTag()) {
            target.setTag(false);
            result.put("Status", "Success");
            result.put("message", "대표 시간표가 해제되었습니다.");
            return result;
        }

        timetablerepository.clearPrimary(userId);

        target.setTag(true);

        result.put("Status", "Success");
        result.put("massage", "대표 시간표가 설정되었습니다.");
        return result;
    }
}
