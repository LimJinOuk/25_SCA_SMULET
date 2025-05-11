package com.jinouk.smulet.domain.homecontrol.controller;

import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.service.memberservice;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/")
    public String website(){return "user/My_page_test";}

    @GetMapping("/Register")
    public String register(){return "user/Register";}

    @GetMapping("/login_page")
    public String loginform(){return "user/login_Page";}

    @GetMapping("/techtree")
    public String techtree(){return "user/techtree_main";}

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
                return "techtree_main";
        }
    }


    @PostMapping("/do_Register")
    public String save(@ModelAttribute userdto userdto)
    {
        mservice.save(userdto);
        return "user/login_page";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String , String>> login(@RequestBody userdto userdto, HttpSession session)
    {
        Map<String , String> map = new HashMap<>();

        System.out.println("1"+ userdto);
        userdto loginresult = mservice.login(userdto);
        if(loginresult!=null) {
            System.out.println(loginresult);
            session.setAttribute("loginEmail" , loginresult.getName());
            map.put("Status" , "success");
            return ResponseEntity.ok(map);
        }
        else{
            System.out.println(loginresult);
            map.put("Status" , "fail");
            return ResponseEntity.ok(map);
        }
    }


}
