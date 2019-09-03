package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Homework;
import com.bitcamp.project.project_4bit.repository.HomeworkRepository;
import com.bitcamp.project.project_4bit.repository.HwArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class HomeworkService {

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Transactional
    public Homework createHomework(Homework homework) {
        return homeworkRepository.save(homework);
    }

    @Transactional
    public Page<Homework> listOfHomework(Pageable pageable) {
        return homeworkRepository.findAll(pageable);
    }

    @Transactional
    public Optional<Homework> itemOfHomework(Long hwId) {
        return homeworkRepository.findById(hwId);
    }

    @Transactional
    public int updateHomework(Long hwId, Homework homework) {

        String newHwName = homework.getHwName();
        Date newHwDeadLine = homework.getHwDeadLine();
        String newHwSubject = homework.getHwSubject();
        String newHwDescription = homework.getHwDescription();

        return homeworkRepository.updateHomework(hwId, newHwName, newHwDeadLine, newHwSubject, newHwDescription);
    }

    @Transactional
    public void deleteHomework(Long id) {
        homeworkRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Homework> listOfHomeworkByClassIdAndDate(Long classId, String requestedTime, Pageable pageable) {
        return homeworkRepository.findAllByClassIdAndRequestedTime(classId, requestedTime, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Homework> listOfEndedHomeworkByClassIdAndDate(Long classId, String requestedTime, Pageable pageable) {
        return homeworkRepository.findAllEndedByClassIdAndRequestedTime(classId, requestedTime, pageable);
    }

    @Transactional(readOnly = true)
    public Homework loadHomeworkByHwId(Long hwId){
        return homeworkRepository.findByHwId(hwId);
    }

}