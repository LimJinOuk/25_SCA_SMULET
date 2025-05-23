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
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/login_page")
    public String loginform(){return "user/login_page";}

    @GetMapping("my_page")
    public String mypage(@AuthenticationPrincipal Object principal, Model model)
    {
        if (principal == null) {
            return "redirect:/login"; // 로그인 안 되어 있으면 리다이렉트
        }

        model.addAttribute("username", principal.toString());
        return "user/mypage/mypage"; // → templates/my_page.html 렌더링
    }

    @GetMapping("/userinfo")
    public Object getUserInfo(@AuthenticationPrincipal Object principal, Model model)
    {
        Map<String , String> map = new HashMap<>();
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", principal.toString());
        return mservice.userInfo(principal.toString());
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
        headers.set("Authorization", token);

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

    @GetMapping("/PWupdate")
    public String to_PWupdata() {return "check_pw_page";}

    @PostMapping("/check_pw_button")
    public ResponseEntity<Map<String, Boolean>> check_PW(@RequestParam String pw, HttpServletRequest request)
    {
        Map<String, Boolean> map = new HashMap<>();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtutil.getUserName(token);
            Boolean result = mservice.checkPW(pw, username);
            map.put("Password", result);
            return ResponseEntity.ok(map);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }
    }

    @PostMapping("/PWupdate_button")
    public ResponseEntity<Map<String, Boolean>> user_update(@RequestParam String new_PW, HttpServletRequest request) {
        Map<String, Boolean> map = new HashMap<>();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtutil.getUserName(token);
            Boolean result = mservice.update_PW(new_PW, username);
            map.put("updateStatus", result);
            return ResponseEntity.ok(map);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }
    }

}
