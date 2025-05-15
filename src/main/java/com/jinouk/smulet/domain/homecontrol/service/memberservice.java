package com.jinouk.smulet.domain.homecontrol.service;


import com.jinouk.smulet.domain.homecontrol.dto.userdto;
import com.jinouk.smulet.domain.homecontrol.entity.user;
import com.jinouk.smulet.domain.homecontrol.repository.loginrepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
@RequiredArgsConstructor
public class memberservice {
    private final loginrepository loginrepository;

    public void save(userdto userdto)
    {
        System.out.println("2"+ userdto);
        user memberentity = user.tomemberentity(userdto);
        System.out.println("4"+memberentity);
        loginrepository.save(memberentity);
    }

    public userdto login(userdto userdto){
        System.out.println("3"+ userdto);
        Optional<user> byemail = loginrepository.findByEmail(userdto.getEmail());

        System.out.println(byemail);
        System.out.println(byemail.isPresent());
        if(byemail.isPresent())
        {
            user memberentity = byemail.get();
            if(memberentity.getPw().equals(userdto.getPw()))
            {
                userdto dto = userdto.tomemberdto(memberentity);
                return dto;
            }
            else
            {return null;}
        }
        else{return null;}
    }


}
