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
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class memberservice {
    private final loginrepository loginrepository;
    private final JWTUtil jwtUtil;

    public void save(userdto userdto)
    {
        user memberentity = user.tomemberentity(userdto);
        loginrepository.save(memberentity);
    }

    public userdto login(userdto userdto) throws IllegalArgumentException
    {
        System.out.println("12"+userdto);
        Optional<user> byemail = loginrepository.findByEmail(userdto.getEmail());

        if(byemail.isPresent())
        {
            user memberentity = byemail.get();
            if(memberentity.getPw().equals(userdto.getPw()))
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


    public boolean update_user(userdto updatedto, HttpServletRequest request) {
        String authHeader = request.getHeader("AUTHORIZATION");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.getUserName(token);

            Optional<user> byname = loginrepository.findByName(username);
            if (byname.isPresent()) {
                user entity = byname.get();
                // 일단 지금 이름, 비번만 수정
                if (updatedto.getName() != null && !updatedto.getName().isBlank()) {
                    entity.setName(updatedto.getName());
                }
                if (updatedto.getPw() != null && !updatedto.getPw().isBlank()) {
                    entity.setPw(updatedto.getPw());
                }
                loginrepository.save(entity);
                return true; // 정보 수정 성공!
            } else {
                return false;
            } // 이럴 경우가 있을지는 모르겠지만 DB에 토큰에 해당하는 이름이 없는 경우
        } else {
            return false;
        } // 유효하지 않은 토큰
    }


    public boolean checkPW(String pw, HttpServletRequest request)
    {
        String authHeader = request.getHeader("AUTHORIZATION");
        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            String token = authHeader.substring(7);
            String username = jwtUtil.getUserName(token);

            Optional<user> byname = loginrepository.findByName(username);
            if (byname.isPresent())
            {
                if (byname.get().getPw().equals(pw)) {return true;} //비번 정답
                else {return false;} // 입력한 비번 틀림
            }
            else {return false;} //DB에 사용자 토큰에 들어있는 이름 없음
        }
        else {return false;} // 유효한 토큰이 아님
    }
}
