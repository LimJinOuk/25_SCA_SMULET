package com.jinouk.smulet.domain.SQLQuery.service;


import com.jinouk.smulet.domain.SQLQuery.repository.getbsm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class getbsmservice {
    private final getbsm bsm;

    public int getbsm(int userId){
       return (bsm.findBsmZeroListByUserId(userId)) * 3;
    }

    public int get설계(int userId){
        return (bsm.findBsmOneListByUserId(userId)) * 3;
    }
}
