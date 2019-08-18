package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.TestGroup;
import com.bitcamp.project.project_4bit.repository.TestGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TestGroupService {

    @Autowired
    private TestGroupRepository testGroupRepository;

    // StudentTestController를 만들면서 추가한 부분
    // 역할 : testGroupRepository에서 얻은  TestGroup 전체 정보 (DB : test_group / java : TestGroup)를 반환
    public TestGroup findOneByTestId(Long userId){
        return testGroupRepository.findOneByUserId(userId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 시험 생성
    @Transactional
    public TestGroup createTestGroup(TestGroup testGroup){
        return testGroupRepository.save(testGroup);
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 시험 진행 중 전체 출력에 필요한 메소드
    //       testGroupRepository에서 얻은 testGroup 정보 전체 (DB : test_group / java : TestGroup)를 넘긴다
    @Transactional(readOnly = true)
    public Page<TestGroup> listOfTestGroup(Long classId, String today, Pageable var1){

        System.out.println("서비스_반_번호 : " + classId);
        System.out.println("서비스_지금_시간 : " + today);

        return testGroupRepository.findTestGroupByClassGroupId(classId, today, var1);
    }


    // 역할 : 시험 진행 완료 전체 출력에 필요한 메소드
    //       testGroupRepository에서 얻은 testGroup 정보 전체 (DB : test_group / java : TestGroup)를 넘긴다
    @Transactional(readOnly = true)
    public Page<TestGroup> listOfEndedTestGroup(Long classId, String today, Pageable var2){
        return testGroupRepository.findEndedTestGroupByClassGroupId(classId, today, var2);
    }


    // 역할 : 시험 하나를 클릭했을 때 시험 설명 출력에 필요한 메소드
    //       testGroupRepository에서 얻은 testGroup 정보 전체 (DB : test_group / java : TestGroup)를 넘긴다
    @Transactional
    public Optional<TestGroup> itemOfTestGroup(Long classId, Long testId){
        return testGroupRepository.findByTestId(classId, testId);

    }

    // 역할 : 시험 응시하기 버튼을 클릭했을 때 가능 기간인지를 구분하기 위한 메소드
    //       testGroupRepository에서 얻은 testId를 넘긴다
    @Transactional(readOnly = true)
    public Long applyItemOfTestGroup(Long classId, Long testId, String today){
        return testGroupRepository.findApplyTestByTestId(classId, testId, today);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 시험 수정
    @Transactional
    public int updateTestGroup(String testName, String testStartTime, String testEndTime, String testDescription, Long testId, Long classId){

        return testGroupRepository.updateTestGroup(testName, testStartTime, testEndTime, testDescription, testId, classId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 시험 삭제
    @Transactional
    public void deleteTestGroup(Long testId){
        testGroupRepository.deleteById(testId);
     }












    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // StudentTest를 만들면서 추가한 부분
    @Transactional
    public TestGroup loadTestGroupBytestId(Long testId){
        return testGroupRepository.findByTestId(testId);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public int updateStudentScore(int sum, double avg, int max, int min,Long testId){
        return testGroupRepository.updateTestGroup(sum, avg, max, min, testId);
    }
}