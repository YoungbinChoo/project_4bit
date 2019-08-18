package com.bitcamp.project.project_4bit.service;
import com.bitcamp.project.project_4bit.entity.PointLog;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.repository.PointLogRepository;
import com.bitcamp.project.project_4bit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// 작성자 : 황서영
@Service
public class PointLogService {

    @Autowired
    private PointLogRepository pointLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<PointLog> listofPointLog(Pageable pageable){
        return  pointLogRepository.findAll(pageable);
    }

    @Transactional
    public PointLog addedPointLog(PointLog pointLog){

        // 얻은 포인트
        int point = pointLog.getPointAdded();

        // user에 있는 pointSum으로 더해준다.
        Long userId = pointLog.getUser().getUserId();

        int updatePoint = userRepository.updatePointSum(userId,point);

        if(updatePoint == 1){
            // PointLog로 Log를 남긴다.
            return  pointLogRepository.save(pointLog);
        }else {
            return null; // 포인트 더하는 업데이트 실패하면 Log도 안남김
        }
    }
}