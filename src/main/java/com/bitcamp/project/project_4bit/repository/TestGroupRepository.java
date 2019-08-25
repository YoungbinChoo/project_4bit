package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.TestGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TestGroupRepository extends JpaRepository<TestGroup, Long> {

    // TestGroupRepositoryTest에서 사용 >> 시험 고유번호로 조회
    TestGroup findByTestId(Long testId);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // StudentTestController를 만들면서 추가한 부분
    //
    @Query(value = "SELECT * FROM test_group WHERE test_id = ?1", nativeQuery = true)
    TestGroup findOneByUserId(Long testId);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 반별 시험을 진행 중인지 구분한다
    //       반별 시험 종료 시간으로 검색한다 >> TestGroup에 있는 정보 전체를 얻은 후 넘김
    //       시험이 진행 중일려면 시험 종료시간이 현재 시간보다 이후여야 하므로 조건식을 test_end_time > today로 한다
    @Query(value = "SELECT * FROM test_group WHERE class_id=?1 AND test_end_time > ?2", nativeQuery = true)
    Page<TestGroup> findTestGroupByClassGroupId(Long classId, String today, Pageable var1);


    // 역할 : 반별 시험을 진행완료인지 구분한다
    //       반별 시험 종료시간으로 검색한다 >> TestGroup에 있는 정보 전체를 얻은 후 넘김
    //       시험이 진행 완료일려면 시험 종료시간이 현재시간보다 이전이어야 하므로 조건식을 test_end_time < today로 한다
    @Query(value = "SELECT * FROM test_group WHERE class_id=?1 AND test_end_time < ?2", nativeQuery = true)
    Page<TestGroup> findEndedTestGroupByClassGroupId(Long classId, String today, Pageable var2);


    // 역할 : 리스트에 있는 시험(하나만)을 클릭했을 때 해당 시험을 반환한다
    //        반별 시험 고유번호로 검색한다 >> TestGroup에 있는 정보 전체를 얻은 후 넘김
    @Query(value = "SELECT * FROM test_group WHERE class_id=?1 AND test_id = ?2 ", nativeQuery = true)
    Optional<TestGroup> findByTestId(Long classId, Long testId);


    // 역할 : 응시가 가능한 기간이면 해당 시험 고유번호를 반환하고 그렇지 않으면 null을 반환한다
    @Query(value = "SELECT test_id FROM test_group WHERE class_id=?1 AND test_id = ?2 AND test_start_time < ?3", nativeQuery = true)
    Long findApplyTestByTestId(Long classId, Long testId, String today);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 시험 수정
    @Modifying
    @Query(value = "UPDATE test_group SET test_name=?1, test_start_time=?2, test_end_time=?3, test_description=?4 WHERE test_id =?5 AND class_id = ?6", nativeQuery = true)
    int updateTestGroup(String testName, String testStartTime, String testEndTime, String testDescription, Long testId, Long classId);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 시험 삭제
    @Modifying
    @Query(value = "DELETE FROM test_group WHERE test_id =?1", nativeQuery = true)
    void deleteByTestId(Long testId);



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 역할 : 해당 시험 총점, 평균, 최고점, 최저점을 넣기 위한 TestGroup 수정
    @Modifying
    @Query(value = "UPDATE test_group SET sum=?1, avg=?2, max=?3, min=?4 WHERE test_id =?5", nativeQuery = true)
    int updateTestGroup(int sum, double avg, int max, int min, Long testId);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 작성된 시험의 user_id (owner)를 찾음
    @Query(value = "SELECT user_id FROM test_group WHERE test_id=?1", nativeQuery = true)
    Long findTestGroupOwner(Long testId);
}