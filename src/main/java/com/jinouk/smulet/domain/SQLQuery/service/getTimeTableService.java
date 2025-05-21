package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.repository.gettimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class getTimeTableService {
    public final gettimeTableRepository timetablerepository;

    public List<getTimeTableDTO> getcoursesByUserID(int userId)
    {
        return timetablerepository.findCoursesByUserId(userId);
    }

    public Map<String, Integer> count_tableID(int userId)
    {
        Integer count = timetablerepository.count_timetableById(userId);
        return Map.of("count", count);
    }
}
