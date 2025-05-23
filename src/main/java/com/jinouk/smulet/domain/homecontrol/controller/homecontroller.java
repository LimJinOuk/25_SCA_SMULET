package com.jinouk.smulet.domain.homecontrol.controller;

import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import com.jinouk.smulet.domain.homecontrol.service.memberservice;
import com.jinouk.smulet.global.jwt.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("my_page")
    public String mypage(@AuthenticationPrincipal Object principal, Model model)
    {
        if (principal == null) {
            return "redirect:/login"; // 로그인 안 되어 있으면 리다이렉트
        }

        model.addAttribute("username", principal.toString());
        return "user/mypage/mypage"; // → templates/my_page.html 렌더링
    }

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
        System.out.println(token);
        String Refresh = jwtutil.generateRefresh(loginresult.getName());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", Refresh)
                .httpOnly(true)
                .secure(false) // HTTPS만 사용할 경우 true TODO : 이거 배포시 꼭 true로 바꾸기
                .path("/")
                .maxAge( 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        headers.set("Authorization",token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .header("Access-Control-Expose-Headers", "Authorization")
                .body(map);

    }

    @PostMapping("/member/delete")
    @ResponseBody
    public ResponseEntity<?> deletemember(@RequestHeader("Authorization") String authHeader)
    {
        System.out.println("컨트롤러 도달");
        System.out.println(authHeader);
        String token = authHeader.startsWith("Bearer") ? authHeader.substring(7) : authHeader;

        System.out.println(token);

        Map<String, String> map = new HashMap<>();

        if(jwtutil.validateToken(token))
        {
            Optional<user> name = loginrepository.findByName(jwtutil.getUserName(token));
            if(name.isPresent())
            {
                user entity = name.get();
                mservice.delete(entity.getEmail());
                map.put("deleteStatus", "success");
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(map);
            }
            else
            {
                map.put("deleteStatus", "fail to communicate with server");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            }
        }
        else
        {
            map.put("deleteStatus", "fail");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        }
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
    @GetMapping("user/info")
    public ResponseEntity<?> getUserInfo(String token) {
        return mservice.userInfo(token);
    }


    @GetMapping("/update")
    public String to_updata() {return "check_pw_page";}

    @GetMapping("/check_pw_button")
    public String check_PW(@RequestParam String pw, HttpServletRequest request)
    {
        if (mservice.checkPW(pw, request)) {return "update_page";}
        else {return "mypage or check_pw_page";}
        //인증 실패 시 돌아가거나 다시 비번 인증 화면으로 돌아가기 프론트에서 편한걸로 선택해
    }

    @PostMapping("/update_button")
    public ResponseEntity<Map<String, String>> user_update(@RequestBody userdto update_dto, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        if (mservice.update_user(update_dto, request)) {
            map.put("update_result", "success");
            return ResponseEntity.ok(map);
        } else {
            map.put("update_result", "fail");
            return ResponseEntity.ok(map);
        }
    }

}
