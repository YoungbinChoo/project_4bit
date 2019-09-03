package com.bitcamp.project.project_4bit.service;

import com.bitcamp.project.project_4bit.entity.Student;
import com.bitcamp.project.project_4bit.entity.Teacher;
import com.bitcamp.project.project_4bit.entity.User;
import com.bitcamp.project.project_4bit.repository.ClassTeacherLogRepository;
import com.bitcamp.project.project_4bit.repository.StudentRepository;
import com.bitcamp.project.project_4bit.repository.TeacherRepository;
import com.bitcamp.project.project_4bit.repository.UserRepository;
import com.bitcamp.project.project_4bit.util.UserIdToClassIdConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// 코드 작성자 : 황서영
// 모든 User의 정보를 등록/ 수정/ 삭제 해주는 관리자의 파트와, 자기 자신의 정보를 읽고 수정하는 강사,학생의 파트로 분리
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassTeacherLogRepository classTeacherLogRepository;

    @Autowired
    private LocalUserDetailsService userDetailsService;

    @Autowired
    private UserIdToClassIdConverter userIdToClassIdConverter;


//=====================================     관리자    ===========================================================

    // 관리자가 반을 선택하면 담당 강사를 뽑아오는 Service
    @Transactional
    public User selectOfTeacher(Long classId){

        // 1. classId로 teacherId 를 찾는다.
        Long teacherId = classTeacherLogRepository.findOneByClassGroup_ClassId(classId);

        // 2. teacherId 로 강사를 찾는다.
        Teacher teacher = teacherRepository.findByTeacherId(teacherId);

        // 3. 찾아온 강사로 유저를 뽑아낸다.
        User user = teacher.getUser();

        return user;
    }


    // 강사가 반을 선택하면 학생을 뽑아오는 Service
    @Transactional
    public Page<Student> listOfStudentByClassId(Long classId,Pageable pageable){

        Page<Student> students = studentRepository.findAllByClassGroup_ClassId(classId, pageable);

        return students;
    }


    // 관리자화면 학생 전체 조회 Service
    @Transactional
    public Page<Student> listOfStudentByAdmin(Pageable pageable){

        Page<Student> students = studentRepository.findAll(pageable);

        return students;
    }

    // 관리자화면 강사 전체 조회 Service
    @Transactional
    public Page<Teacher> listOfTeacherByAdmin(Pageable pageable){

        Page<Teacher> teachers = teacherRepository.findAll(pageable);

        return teachers;
    }


    // 관리자가 유저(강사,학생) 1명만 찾는 Service
    @Transactional(readOnly = true)
    public Optional<User> itemOfUser(Long userId){
        return userRepository.findById(userId);
    }

    // 관리자가 유저를 삭제하는 Service
    @Transactional
    public void deleteUser(Long userId){

        userRepository.deleteById(userId);
    }


    // 관리자가 학생과 강사 개인정보를 수정하는 service
    @Transactional
    public String updateUserByAdmin(Long userId, User user) {

        String newUsername = user.getUsername();
        String newPassword = user.getPassword();
        String newName = user.getName();
        String newEmail = user.getEmail();
        String newPhone = user.getPhone();
        String newBirth = user.getStudentBirth();
        Long newClassId = user.getClassId();


        // 일단 학생 고유의 정보부터 업데이트 후 성공여부를 0 or 1로 받아서 isStudentUpdateSuccess에 대입
        int isStudentUpdateSuccess = studentRepository.updateStudentByAdmin(userId, newBirth, newClassId);

        // 학생 고유정보 업데이트 성공시, 유저정보도 업데이트 시도 (결과값 0 or 1)
        if(isStudentUpdateSuccess==1){
            int isUserUpdateSuccess = userRepository.updateUserByAdmin(userId, newUsername, newPassword, newName, newEmail, newPhone);
            if(isUserUpdateSuccess==1) {
                return "모든 유저정보를 성공적으로 수정하였습니다";
            }
            else if(isUserUpdateSuccess==0) {
                return "학생고유정보는 업데이트 했지만, 유저정보는 수정 실패하였습니다";
            }
            else {
                return "알 수 없는 이유로 유저정보 업데이트에 실패했습니다";
            }
        }
        // 학생 고유정보 업데이트 안했을 경우 강사 업데이트 함
        else if(isStudentUpdateSuccess==0) {

            int isUserUpdateSuccess = userRepository.updateUserByAdmin(userId, newUsername, newPassword, newName, newEmail, newPhone);
            if(isUserUpdateSuccess==1) {
                return "모든 유저정보를 성공적으로 수정하였습니다";
            }else {
                return "알 수 없는 이유로 유저정보 업데이트에 실패했습니다";
            }
        }
        else {
            return "알 수 없는 이유로 학생 고유정보 업데이트에 실패하여, 유저정보도 업데이트 하지 않았습니다";
        }
    }

//=====================================     학생/강사   ===========================================================


    // 유저 정보 변경 후 새로운 유저 반환하려 만들어 봄
    @Transactional
    public User updateNewUser(Long userId){
        return userRepository.findByUserId(userId);
    }


    // 본인(학생/강사)이 본인의 개인정보를 수정하는 service
    @Transactional
    public String updateUserBySelf(Long userId, User user){

        String newPassword = user.getPassword();
        String newEmail = user.getEmail();
        String newPhone = user.getPhone();

        // userId로 user덩어리 받아오기
        User userCheck = (User) userDetailsService.loadUserByUsername(userRepository.findByUserId(userId).getUsername());

        //학생일 경우 birth 수정 가능
        // userCheck 검사결과 학생이면 생일정보 수정
        if (userCheck.getRole().getRoleCode().equals("role_student")) {
            String newBirth = user.getStudentBirth();
            int isStudentUpdateSuccess = studentRepository.updateStudentBySelf(userId, newBirth);
            if(isStudentUpdateSuccess == 1){
                int isUserUpdateSuccess = userRepository.updateUserBySelf(userId, newPassword, newEmail, newPhone);
                if(isUserUpdateSuccess==1) {
                    return "학생 고유정보와 유저정보를 성공적으로 수정하였습니다";
                }
                else if(isUserUpdateSuccess==0) {
                    return "학생고유정보는 업데이트 했지만, 유저정보는 수정 실패하였습니다";
                }else {
                    return "알 수 없는 이유로 유저정보 업데이트에 실패했습니다";
                }
            }else if(isStudentUpdateSuccess == 0){
                return "학생 고유정보 업데이트에 실패하여, 유저정보도 업데이트 하지 않았습니다";
            }else {
                return "알 수 없는 이유로 학생 고유정보 업데이트에 실패하여, 유저정보도 업데이트 하지 않았습니다";
            }
        }else{ // 학생이 아닐 경우 유저 정보만 수정해서 리턴
            int isUserUpdateSuccess = userRepository.updateUserBySelf(userId, newPassword, newEmail, newPhone);
            if(isUserUpdateSuccess==1) {
                return "유저정보를 성공적으로 수정하였습니다";
            }else{
                return "유저정보 업데이트에 실패했습니다";
            }
        }
    }

    // 강사 학생현황 화면에서 counsel 관리하는 서비스================================================================


    // 강사의 userId로 담당 클래스를 찾는 서비스
    @Transactional
    public Long loadClassIdByUserId(Long userId){
        return userIdToClassIdConverter.userIdToClassId(userId);
    }

    //학생 아이디로 담당 클래스를 찾는 서비스
    @Transactional
    public Long loadClassIdByStudentId(Long studentId){

        Student student = studentRepository.findByStudentId(studentId);

        Long userId = student.getUser().getUserId();
        Long classId = userIdToClassIdConverter.userIdToClassId(userId);
        return classId;
    }


    //강사가 학생을 선택하면 counsel 내용을 찾아오는 서비스
    @Transactional
    public String loadCounselByStudentId(Long studentId){
        return studentRepository.findCounselByStudentId(studentId);
    }

    // 강사가 학생을 선택하면 그 학생을 넘겨주는 서비스
    @Transactional
    public Student loadStudentByStudentId(Long studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    //강사가 학생의 counsel 내용을 작성, 수정하는 서비스
    @Transactional
    public int updateCounselByTeacher(Long studentId, String counsel){
        int updateNewCounsel = studentRepository.updateCounsel(studentId,counsel);

        if(updateNewCounsel == 1){
            return 1;
        }else {
            return 0;
        }
    }


    @Transactional(readOnly = true)
    public User loadUserByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone);
    }

    @Transactional
    public int updateUserPassword(Long userId, String tempPassword) {
        return userRepository.updateUserPassword(userId, tempPassword);
    }
}
