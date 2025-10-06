package com.jinouk.smulet.domain.SQLQuery.controller;

import com.jinouk.smulet.domain.SQLQuery.dto.addTCDTO;
import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.repository.ScheduleCourseRow;
import com.jinouk.smulet.domain.SQLQuery.service.*;
import com.jinouk.smulet.domain.SQLQuery.dto.getTS2TEDTO;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final deletetimetable deletetimetable;
    private final getTcService service;
    private final getTimeTableService tservice;
    private final loginrepository loginrepository;
    private final getCourseTS2TE TS2TE;
    private final getbsmservice getbsmservice;
    private final get전심전선 get전심전선;

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

    @PostMapping("/getTC")
    public ResponseEntity<?> getTC(@RequestBody int userId) {
        var rows = service.getAllRowsByUserId(userId);
        return ResponseEntity.ok(rows);
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

    @PostMapping("/deleteTimetable")
    public ResponseEntity<?> deleteTimetable(@RequestBody int timetableId) {
        Map<String, String> map = new HashMap<>();
        int affected = deletetimetable.deleteByIdNative(timetableId);
        return affected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/addTC")
    public ResponseEntity<?> setTimetableTC(@RequestBody addTCDTO timetableDTO) {
        Map<String , String> map = new HashMap<>();
        try{
            settimetablecourseservice.settimetableCourse(timetableDTO.getTimetableId(), timetableDTO.getCourseIds());

        }
        catch (Exception e) {
            map.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
        map.put("Status" , "Success");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/tableId_List")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> send_tableID_count(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        Optional<user> Byname = loginrepository.findByName(username);
        Integer userid = Byname.get().getId();
        Map<String, List<Map<String, Object>>> map = tservice.find_tableIDs(userid);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/representative_Timetable")
    public ResponseEntity<?> representative_Timetable(Model model, @RequestParam int timetableId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        Optional<user> Byname = loginrepository.findByName(username);
        Integer userid = Byname.get().getId();
        Map<String, String> map = tservice.representativeTimeTable(userid, timetableId);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/getTS2TE")
    public ResponseEntity<List<ScheduleCourseRow>> getTimesByIds(@RequestBody getTS2TEDTO request) {
        return ResponseEntity.ok(TS2TE.getTimesByIds(request.getGetScheduleIds()));
    }

    @PostMapping("/getbsm")
    public ResponseEntity<?> getbsm()
    {
        return  ResponseEntity.ok(getbsmservice.getbsm());
    }

    @PostMapping("/get설계")
    public ResponseEntity<?> get설계()
    {
        return  ResponseEntity.ok(getbsmservice.get설계());
    }

    @PostMapping("/get전심")
    public ResponseEntity<?> get전심()
    {
        return  ResponseEntity.ok(get전심전선.get전심());
    }

    @PostMapping("/get전선")
    public ResponseEntity<?> get전선()
    {
        return  ResponseEntity.ok(get전심전선.get전선());
    }
}
