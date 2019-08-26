package com.bitcamp.project.project_4bit.repository;

import com.bitcamp.project.project_4bit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


// 작성자 : 황서영
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 1. 유저를 username/userId/email로 찾는 쿼리
    User findByUsername(String username);

    User findByUserId(Long userId);

    User findByEmail(String email);

    // 2. userId를 통해 찾는 쿼리
    Optional<User> findById(Long userId);

    // 3. 유저(학생/강사)가 직접 자신의 개인정보를 수정하는 쿼리
    @Modifying
    @Query(value = "UPDATE user u SET u.password=?2, u.email=?3, u.phone=?4 WHERE u.user_id =?1", nativeQuery = true)
    int updateUserBySelf(Long userId, String newPassword, String newEmail, String newPhone);

    // 4. admin이 user를 수정하는 쿼리
    @Modifying
    @Query(value = "UPDATE user u SET u.username=?2, u.password=?3, u.name=?4, u.email=?5, u.phone=?6 WHERE u.user_id =?1", nativeQuery = true)
    int updateUserByAdmin(Long userId, String newUsername, String newPassword, String newName, String newEmail, String newPhone);


    //5. Point가 더해지면 User에 Point 더하는 쿼리
    @Modifying
    @Query(value = "UPDATE user u SET u.point_sum = u.point_sum+?2 WHERE u.user_id =?1", nativeQuery = true)
    int updatePointSum(Long userId, int point);

    // 6. Password 변경하는 쿼리
    @Modifying
    @Query(value = "UPDATE user u SET u.password =?2 WHERE u.user_id=?1", nativeQuery = true)
    int updateUserPassword(Long userId, String password);
}
