package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.RoadmapExercise;
import com.bitcamp.project.project_4bit.repository.RoadmapExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

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

    // 역할    : ExerciseSequence를 랜덤생성하여 문제 출제
    @Transactional(readOnly = true)
    public RoadmapExercise itemOfRoadmapExerciseAndExerciseSequence(Integer roadmapStageNo){
        Long[] exerciseArray = roadmapExerciseRepository.findByRoadmapExercise_ExerciseSequence(roadmapStageNo);
        int randomNum = new Random().nextInt(5);
        System.out.println("랜덤생성된 숫자: " + randomNum);

        Long selectedExerciseSequence = exerciseArray[randomNum];
        System.out.println("선택된 ExerciseSequence: " + selectedExerciseSequence);

        return roadmapExerciseRepository.findByExerciseSequence(selectedExerciseSequence);
    }

}
