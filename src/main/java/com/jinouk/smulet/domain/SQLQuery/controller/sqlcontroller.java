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

    @GetMapping("/course{userId}") //사용자 id값 가져오기 사용자 값이 없다면 오류 발생
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

    @GetMapping("/a") //모든 과목 조회 기능
    public List<getTimeTableDTO> getAllCourseList (@RequestParam int semester) {
        List<getTimeTableDTO> courseList = sqlservice.allCourse(semester);
        return courseList;
    }

    @PostMapping("/getTC") //특정 사용자id로부터 해당 사용자가 가지고 있는 시간표에 저장된 과목 조회
    public ResponseEntity<?> getTC(@RequestBody int userId) {
        var rows = service.getAllRowsByUserId(userId);
        return ResponseEntity.ok(rows);
    }

    @PostMapping("/addTimetable") // 시간표추가
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

    @PostMapping("/deleteTimetable") //특정 시간표 삭제
    public ResponseEntity<?> deleteTimetable(@RequestBody int timetableId) {
        Map<String, String> map = new HashMap<>();
        int affected = deletetimetable.deleteByIdNative(timetableId);
        return affected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/addTC") // 특정 시간표에 과목 추가
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

    @GetMapping("/tableId_List") // 특정 사용자 id로부터 해당 사용자가 가진 시간표 id들 반환
    public ResponseEntity<Map<String, List<Map<String, Object>>>> send_tableID_count(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        Optional<user> Byname = loginrepository.findByName(username);
        Integer userid = Byname.get().getId();
        Map<String, List<Map<String, Object>>> map = tservice.find_tableIDs(userid);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/representative_Timetable") //대표시간표 설정하기
    public ResponseEntity<?> representative_Timetable(Model model, @RequestParam int timetableId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        Optional<user> Byname = loginrepository.findByName(username);
        Integer userid = Byname.get().getId();
        Map<String, String> map = tservice.representativeTimeTable(userid, timetableId);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/getTS2TE") // TimeTable Schedule을 반환해주는 기능 --> 특정 사용자가 가진 특정 시간표에 저장돼 있는 과목의 상세 정보 반환
    public ResponseEntity<List<ScheduleCourseRow>> getTimesByIds(@RequestBody getTS2TEDTO request) {
        return ResponseEntity.ok(TS2TE.getTimesByIds(request.getGetScheduleIds()));
    }

    @PostMapping("/getbsm") // bsm인지 확인하고 bsm이라면 학점 수인 3을 곱해서 반환 ex. bsm이 1개 있다면 3을 반환 , 2개 있다면 6을 반환
    public ResponseEntity<?> getbsm(@RequestBody int userId) {
        System.out.println("bsm"+ getbsmservice.getbsm(userId));
        return  ResponseEntity.ok(getbsmservice.getbsm(userId));}

    @PostMapping("/get설계") // 설계인지 확인하고 설계라면 학점 수인 3을 곱해서 반환 ex. 설계가 1개 있다면 3을 반환 , 2개 있다면 6을 반환
    public ResponseEntity<?> get설계(@RequestBody int userId) {
        System.out.println("설계"+ getbsmservice.get설계(userId));
        return  ResponseEntity.ok(getbsmservice.get설계(userId));}

    @PostMapping("/get전심") // 전심인지 확인하고 전심이라면 학점 수인 3을 곱해서 반환 ex. 전심이 1개 있다면 3을 반환 , 2개 있다면 6을 반환
    public ResponseEntity<?> get전심(@RequestBody int userId)
    {
        System.out.println("전심"+ get전심전선.get전심(userId));
        return  ResponseEntity.ok(get전심전선.get전심(userId));
    }

    @PostMapping("/get전선") // 전선인지 확인하고 전선이라면 학점 수인 3을 곱해서 반환 ex. 전선이 1개 있다면 3을 반환 , 2개 있다면 6을 반환
    public ResponseEntity<?> get전선(@RequestBody int userId)
    {
        System.out.println("전선"+ get전심전선.get전선(userId));
        return  ResponseEntity.ok(get전심전선.get전선(userId));
    }
}
