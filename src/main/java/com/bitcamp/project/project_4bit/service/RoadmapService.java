package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Roadmap;
import com.bitcamp.project.project_4bit.repository.RoadmapRepository;
import com.bitcamp.project.project_4bit.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoadmapService {

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 역할    : Roadmap 내용 전체출력
    // 설명    : findAll 은 Page 인터페이스 안에 구현되있는 구현체이다.
    @Transactional
    public Page<Roadmap> listOfRoadmapStageNo(Pageable pageable){

        return roadmapRepository.findAll(pageable);
    }

    // 역할    : RoadmapStageNo를 통해 내용 상세보기
    @Transactional(readOnly = true)
    public Integer itemOfRoadmapAndRoadmapStageNo(Integer roadmapStageNo){
        return roadmapRepository.findByRoadmap_RoadmapStageNo(roadmapStageNo);
    }

    // 로드맵 마지막단계 수정
//    @Transactional
//    public String updateStudentBySelf(Long studentId, Student student) {
//        // studentId로 student덩어리 받아오기
//        Roadmap roadmapStageNo = (Roadmap) RoadmapExerciseService(StudentRepository.findByStudentId(studentId).getUsername());
//        Integer newRoadmapLast = student.getRoadmapLast();
//        int isStudentUpdateSuccess = studentRepository.updateStudentBySelf(studentId, newRoadmapLast);
//        return "업데이트 완료";
//    }
    public int updateCounselByTeacher(Long studentId, Integer roadmapLast) {
        int updateNewRoadmapLast = studentRepository.updateRoadmapLast(studentId, roadmapLast);
        if(updateNewRoadmapLast == 1){
            return 1;
        }else {
            return 0;
        }
    }
}
