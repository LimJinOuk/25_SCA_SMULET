package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.repository.deleteTimetableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class deletetimetable {

    private final deleteTimetableRepository deletetimetableRepository;

    @Transactional
    public int deleteByIdNative(Integer id) {
        return deletetimetableRepository.deletetimetable(id);
    }

}
