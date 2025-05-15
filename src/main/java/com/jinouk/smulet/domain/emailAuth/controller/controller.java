package com.jinouk.smulet.domain.emailAuth.controller;

import com.jinouk.smulet.domain.emailAuth.dto.dto;
import com.jinouk.smulet.domain.emailAuth.dto.dto_code;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jinouk.smulet.domain.emailAuth.service.service;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Component
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
            result.put(dto.getEmail(), code);
            result.put("Status", "Code_Generated");
            return ResponseEntity.ok(result);
        }
        else
        {
            result.put("Status", "Code_Not_Generated");
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/check_code")
    @ResponseBody
    public ResponseEntity<Map<String , String>> checkCode(@RequestBody dto_code dto_code) {
        Map<String, String> map = new HashMap<>();
        dto_code result = service.check_code(dto_code);
        if (result != null) {
            map.put("Status", "Code_correct");
            return ResponseEntity.ok(map);
        }
        else {
            map.put("Status", "Code_incorrect");
            return ResponseEntity.ok(map);
        }
    }
}


