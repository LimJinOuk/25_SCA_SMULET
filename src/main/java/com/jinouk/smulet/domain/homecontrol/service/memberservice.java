package com.jinouk.smulet.domain.homecontrol.service;


import com.jinouk.smulet.domain.SQLQuery.dto.TimetableDto;
import com.jinouk.smulet.domain.SQLQuery.entity.timetable;
import com.jinouk.smulet.domain.SQLQuery.repository.gettimeTableRepository;
import com.jinouk.smulet.domain.homecontrol.dto.UserInfoDto;
import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import com.jinouk.smulet.global.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class memberservice {
    private final loginrepository loginrepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final gettimeTableRepository gettimeTableRepository;

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

    public ResponseEntity<UserInfoDto> userInfo(String name)throws IllegalArgumentException
    {
        Optional<user> entity = loginrepository.findByName(name);
        if(entity.isPresent())
        {
            user A = entity.get();
            List<timetable> timetablesEntity = gettimeTableRepository.findAllByUserId_Id(A.getId());
            List<TimetableDto> timetablesDTO = timetablesEntity.stream()
                    .map(TimetableDto::entityToDto)
                    .collect(Collectors.toList());

            UserInfoDto user = UserInfoDto.builder()
                    .name(A.getName())
                    .email(A.getEmail())
                    .timetables(timetablesDTO)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(user);
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
        Optional<user> byname = loginrepository.findByName(username);
        if (byname.isPresent()) {
            return passwordEncoder.matches(pw, byname.get().getPw());
        }
        else {return false;} //DB에 사용자 토큰에 들어있는 이름 없음
    }
}
