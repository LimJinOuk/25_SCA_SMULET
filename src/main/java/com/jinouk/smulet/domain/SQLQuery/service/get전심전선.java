package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.repository.get전심전선repo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class get전심전선 {
    private final get전심전선repo 전심전선;

    public List<String> get전심(){
        return 전심전선.findBsmOrDesignEqualsZero();
    }

    public List<String> get전선(){
        return 전심전선.findBsmOrDesignEqualsOne();
    }
}
