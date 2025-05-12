package com.jinouk.smulet.domain.emailAuth.controller;

import com.jinouk.smulet.domain.emailAuth.dto.dto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jinouk.smulet.domain.emailAuth.service.service;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class controller {

    private final service service;

    @PostMapping("/send_code")
    public ResponseEntity<Map<String , String>> sendCode(@RequestBody dto dto) throws Exception
    {
        String code = service.sendSimpleMessage(dto.email);
        System.out.println("사용자에게 발송한 코드: " + code);
        Map<String , String> result = new HashMap<>();

        if(code != null)
        {
            result.put("Status", "Code_Generated");
            return ResponseEntity.ok(result);
        }
        else
        {
            result.put("Status", "Code_Not_Generated");
            return ResponseEntity.ok(result);
        }
    }

}


