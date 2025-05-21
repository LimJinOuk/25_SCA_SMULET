package com.jinouk.smulet.domain.homecontrol.controller;

import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import com.jinouk.smulet.domain.homecontrol.service.memberservice;
import com.jinouk.smulet.global.jwt.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    private final JWTUtil jwtutil;

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
        HttpHeaders headers = new HttpHeaders();

        userdto loginresult = mservice.login(userdto);
        map.put("login_result", "success");
        System.out.println(loginresult);

        System.out.println(loginresult.getName());
        String token = jwtutil.generateToken(loginresult.getName());
        String Refresh = jwtutil.generateRefresh(loginresult.getName());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", Refresh)
                .httpOnly(true)
                .secure(false) // HTTPS만 사용할 경우 true TODO : 이거 배포시 꼭 true로 바꾸기
                .path("/")
                .maxAge( 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        headers.set("Authorization", "Bearer" + token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .header("Access-Control-Expose-Headers", "Authorization")
                .body(map);

    }

    @PostMapping("/member/delete")
    public String deletemember(HttpSession session) {
        userdto loginUser = new userdto();
        loginUser.setEmail((String) session.getAttribute("loginEmail")); //setAttribute한 loginEmail, getattribute
        ;
        if (loginUser != null)
        {
            mservice.delete(loginUser.getEmail());
            session.invalidate();
        }

        return "redirect:/login_page";
    }

    @PostMapping("/refreshT")
    public ResponseEntity<?> refresh(HttpServletRequest request)
    {
        Map<String, String> map = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if ("refreshToken".equals(cookie.getName()))
                {
                    refreshToken = cookie.getValue();
                }
            }
            if (refreshToken != null)
            {
                if (jwtutil.validateRefresh(refreshToken)) {
                    String newAccessToken = jwtutil.generateToken(jwtutil.getUserName(refreshToken));
                    headers.set("Authorization", "Bearer " + newAccessToken);
                    map.put("status", "success");
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .headers(headers)
                            .header("Access-Control-Expose-Headers", "Authorization")
                            .body(map);
                } else {
                    throw new JwtException("Invalid Refresh Token");
                }
            }
            else {throw new JwtException("Refresh token not found");}

        } else {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");}
    }
}
