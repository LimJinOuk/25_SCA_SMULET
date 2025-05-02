package com.jinouk.smulet.domain.homecontrol.controller;

import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.service.memberservice;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class homecontroller {
    private final memberservice mservice;

    @GetMapping("/")
    public String website(){return "user/My_page";}

    @GetMapping("/Register")
    public String register(){return "user/Register";}

    @GetMapping("/login_page")
    public String loginform(){return "user/login_Page";}

    @PostMapping("/Register")
    public String save(@ModelAttribute userdto userdto)
    {
        mservice.save(userdto);
        return "user/login_page";
    }

    @PostMapping("/login_page")
    public String login(@ModelAttribute userdto userdto, HttpSession session)
    {
        System.out.println("1"+ userdto);
        userdto loginresult = mservice.login(userdto);
        if(loginresult!=null) {
            System.out.println(loginresult);
            session.setAttribute("loginEmail" , loginresult.getName());
            return "user/My_page";
        }
        else{
            System.out.println(loginresult);
            return "user/login_page";
        }
    }


}
