package com.bitcamp.project.project_4bit.service;
import com.bitcamp.project.project_4bit.entity.PointLog;
import com.bitcamp.project.project_4bit.repository.PointLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class PointLogService {
    @Autowired
    private PointLogRepository pointLogRepository;
    @Transactional(readOnly = true)
    public Page<PointLog> listofPointLog(Pageable pageable){
        return  pointLogRepository.findAll(pageable);
    }
    @Transactional
    public PointLog addedPointLog(PointLog pointLog){
        return  pointLogRepository.save(pointLog);
    }
}