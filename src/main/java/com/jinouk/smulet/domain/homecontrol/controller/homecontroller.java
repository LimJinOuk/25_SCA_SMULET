package com.jinouk.smulet.domain.homecontrol.controller;

import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import com.jinouk.smulet.domain.homecontrol.service.memberservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class homecontroller {
    private final memberservice mservice;
    private final loginrepository loginrepository;

    @GetMapping("/")
    public String website(){return "user/main";}

    @GetMapping("/Register")
    public String register(){return "user/Register";}

    @GetMapping("/login_page")
    public String loginform(){return "user/login_page";}
  
    @GetMapping("/techtree")
    public String techtree(){return "user/main";}

    @GetMapping("/year")
    public String tech_tree(@RequestParam String year) {
        switch (year) {
            case "21":
                return "techtree/tech_tree_21";
            case "22":
                return "techtree/tech_tree_22";
            case "23":
                return "techtree/tech_tree_23";
            case "24":
                return "techtree/tech_tree_24";
            case "25":
                return "techtree/tech_tree_25";
            default:
                return "user/main";
        }
    }



    @PostMapping("/do_Register")
    public ResponseEntity<Map<String , String>> save(@RequestBody userdto userdto)
    {
        Map<String , String> map = new HashMap<>();
        if(loginrepository.findByEmail(userdto.getEmail()).isEmpty())
        {
            mservice.save(userdto);
            map.put("status", "success");
            return ResponseEntity.ok(map);
        }
        else
        {
            throw new IllegalArgumentException("email already in use");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String , String>> login(@RequestBody userdto userdto)
    {
        Map<String , String> map = new HashMap<>();

        userdto loginresult = mservice.login(userdto);

        if(loginresult!=null)
        {
            System.out.println(loginresult);
            map.put("Status" , "success");
            return ResponseEntity.ok(map);
        }
        else
        {
            throw new IllegalArgumentException("알 수 없는 이유로 로그인 실패");
        }
    }


}
