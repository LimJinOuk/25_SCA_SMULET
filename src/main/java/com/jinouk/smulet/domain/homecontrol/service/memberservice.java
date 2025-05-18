package com.jinouk.smulet.domain.homecontrol.service;


import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
@RequiredArgsConstructor
public class memberservice {
    private final loginrepository loginrepository;

    public void save(userdto userdto)
    {
        user memberentity = user.tomemberentity(userdto);
        loginrepository.save(memberentity);
    }

    public userdto login(userdto userdto) throws IllegalArgumentException
    {
        System.out.println(userdto);
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


}
