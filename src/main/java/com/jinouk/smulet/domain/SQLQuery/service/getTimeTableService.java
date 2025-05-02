package com.jinouk.smulet.domain.SQLQuery.service;

import com.jinouk.smulet.domain.SQLQuery.dto.getTimeTableDTO;
import com.jinouk.smulet.domain.SQLQuery.repository.gettimeTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class getTimeTableService {
    public final gettimeTableRepository timetablerepository;

    //사용자 ID값을 기준으로 시간표 조회하기
    //조회되는 값은 /main/resource/sql/check.sql참조
    public List<getTimeTableDTO> getcoursesByUserID(int userId)
    {return timetablerepository.findCoursesByUserId(userId);}
}
