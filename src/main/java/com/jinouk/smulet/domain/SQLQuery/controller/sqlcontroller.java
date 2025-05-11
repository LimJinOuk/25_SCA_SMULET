package com.jinouk.smulet.domain.SQLQuery.controller;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.service.getTimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class sqlcontroller {

    private final getTimeTableService sqlservice;

    @GetMapping("/course{userId}")
    @ResponseBody
    public List<getTimeTableDTO> getcoursesByUserID(@RequestParam(required = false) Integer userId ) {
        if(userId == null){
            userId = 1;
        }

       return sqlservice.getcoursesByUserID(userId);
    }
}
