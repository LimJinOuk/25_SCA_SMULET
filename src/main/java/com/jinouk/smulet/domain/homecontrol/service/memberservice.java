package com.jinouk.smulet.domain.homecontrol.service;


import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import com.jinouk.smulet.global.jwt.JWTUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class memberservice {
    private final loginrepository loginrepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public void save(userdto userdto)
    {
        user user = new user();
        user.setEmail(userdto.getEmail());
        user.setName(userdto.getName());
        String rawPassword = userdto.getPw();
        user.setPw(passwordEncoder.encode(rawPassword));
        loginrepository.save(user);
    }

    public userdto login(userdto userdto) throws IllegalArgumentException
    {
        System.out.println("12"+userdto);
        Optional<user> byemail = loginrepository.findByEmail(userdto.getEmail());

        if(byemail.isPresent())
        {
            user memberentity = byemail.get();
            if(passwordEncoder.matches(userdto.getPw(), memberentity.getPw()))
            {
                userdto dto = userdto.tomemberdto(memberentity);
                return dto;
            }
            else {throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");}
        }
        else{throw new IllegalArgumentException("이메일이 조회되지 않습니다.");}
    }

    @Transactional
    public void delete(String email){
        loginrepository.deleteByEmail(email);
    }

    public ResponseEntity<?> userInfo(String name)throws IllegalArgumentException
    {
        Map<String, String> map = new HashMap<>();
        Optional<user> entity = loginrepository.findByName(name);
        if(entity.isPresent())
        {
            user A = entity.get();
            map.put("name", A.getName());
            map.put("email", A.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        else{
            throw new IllegalArgumentException("사용자가 검색되지 않습니다.");
        }
    }


    public boolean update_PW(String new_PW, String username) {
        Optional<user> byname = loginrepository.findByName(username);
        if (byname.isPresent()) {
            user entity = byname.get();
            entity.setPw(passwordEncoder.encode(new_PW));
            loginrepository.save(entity);
            return true; // 정보 수정 성공!
        } else {
            return false;
        }
    }


    public boolean checkPW(String pw, String username)
    {
        System.out.println(pw);
        Optional<user> byname = loginrepository.findByName(username);
        System.out.println(byname.isPresent());
        if (byname.isPresent()) {
            System.out.println(byname.get().getPw());
            System.out.println(byname.get().getPw().equals(pw));
            return passwordEncoder.matches(pw, byname.get().getPw());
        }
        else {return false;} //DB에 사용자 토큰에 들어있는 이름 없음
    }
}
