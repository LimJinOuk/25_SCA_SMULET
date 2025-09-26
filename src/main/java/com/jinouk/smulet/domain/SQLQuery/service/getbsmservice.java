package com.jinouk.smulet.domain.SQLQuery.service;


import com.jinouk.smulet.domain.SQLQuery.repository.getbsm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class getbsmservice {
    private final getbsm bsm;

    public List<String> getbsm(){
       return bsm.findBsmOrDesignEqualsZero();
    }

    public List<String> get설계(){
        return bsm.findBsmOrDesignEqualsOne();
    }
}
