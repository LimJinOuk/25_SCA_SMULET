package com.jinouk.smulet.domain.SQLQuery.controller;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.dto.setTimetableCourseDTO;
import com.jinouk.smulet.domain.SQLQuery.entity.timetable;
import com.jinouk.smulet.domain.SQLQuery.repository.gettimeTableRepository;
import com.jinouk.smulet.domain.SQLQuery.service.getTimeTableService;
import com.jinouk.smulet.domain.SQLQuery.service.setTimetableCourseService;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class sqlcontroller {

    private final getTimeTableService sqlservice;
    private final setTimetableCourseService settimetablecourseservice;

    @GetMapping("/course{userId}")
    public List<getTimeTableDTO> getcoursesByUserID(@RequestParam(required = false) Integer userId )
    {
        if(userId == null)
        {
            throw new IllegalArgumentException("user id is null");
        }
        else
        {
            return sqlservice.getcoursesByUserID(userId);
        }
    }

    @GetMapping("/a")
    public List<getTimeTableDTO> getAllCourseList () {
        List<getTimeTableDTO> courseList = sqlservice.allCourse();
        return courseList;
    }

    @PostMapping("/addTimetable")
    public ResponseEntity<?> addTimetable (Principal principal, @RequestParam int semester) {
        Map<String, String> map = new HashMap<>();
        String username = principal.getName();
        try {
            sqlservice.addNewTimetable(username, semester);
            map.put("message", "새로운 시간표 생성 성공");
            return ResponseEntity.ok(map);
        } catch (UsernameNotFoundException e) {
            map.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
    }

    @PostMapping("/addTC")
    public ResponseEntity<?> setTimetableTC(@RequestBody setTimetableCourseDTO settimetableCourseDTO) {
        Map<String , String> map = new HashMap<>();

        try{
            settimetablecourseservice.settimetableCourse(settimetableCourseDTO.getTimetableId() , settimetableCourseDTO.getCourseId());

        }
        catch (Exception e) {
            map.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
        map.put("Status" , "Success");
        return ResponseEntity.ok(map);
    }
}
