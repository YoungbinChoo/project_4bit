package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.RoadmapExercise;
import com.bitcamp.project.project_4bit.repository.RoadmapExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoadmapExerciseService {

    @Autowired
    private RoadmapExerciseRepository roadmapExerciseRepository;

    // 역할    : RoadmapExercise 내용 전체출력
    // 설명    : findAll 은 Page 인터페이스 안에 구현되있는 구현체이다.
    @Transactional
    public Page<RoadmapExercise> listOfExerciseSequence(Pageable pageable){

        return roadmapExerciseRepository.findAll(pageable);
    }

    // 역할    : ExerciseSequence를 통해 내용 상세보기
    @Transactional(readOnly = true)
    public Long itemOfRoadmapExerciseAndExerciseSequence(Long exerciseSequence){
        return roadmapExerciseRepository.findByRoadmapExercise_ExerciseSequence(exerciseSequence);
    }

}
