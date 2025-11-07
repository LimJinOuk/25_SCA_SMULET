package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.repository.get전심전선repo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class get전심전선 {
    private final get전심전선repo 전심전선;

    public int get전심(int userId){
        return (전심전선.countDesignZeroUniqueCourseByUser(userId)) * 3;
    }

    public int get전선(int userId){
        return (전심전선.countDesignOneUniqueCourseByUser(userId)) * 3;
    }
}
