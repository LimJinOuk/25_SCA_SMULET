package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.repository.gettimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Map<Integer, List<Integer>> find_tableIDs(Integer userId)
    {
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Integer> list = timetablerepository.findTableIdsByUserId(userId);
        map.put(userId, list);
        return map;
    }
}
