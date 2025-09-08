package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.repository.getTimetableRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class getTcService {
    private final getTimetableRepo repo;

    public List<getTimetableRepo.TimetableCourseRow> getAllRowsByUserId(Integer userId) {
        return repo.findAllByUserIdAsRows(userId);
    }
}
