package com.jinouk.smulet.domain.SQLQuery.controller;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.service.getTimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class sqlcontroller {

    private final getTimeTableService sqlservice;

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

    @GetMapping("/my_page1")
    public ResponseEntity<Map<Integer, List<Integer>>> send_tableID_count(@RequestParam(required = false) Integer userId )
    {
        if (userId == null)
        {
            throw new IllegalArgumentException("user id is null");
        }
        else
        {
            Map<Integer, List<Integer>> map = sqlservice.find_tableIDs(userId);
            return ResponseEntity.ok(map);
        }
    }
}
