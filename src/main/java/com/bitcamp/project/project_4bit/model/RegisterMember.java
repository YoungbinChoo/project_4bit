package com.bitcamp.project.project_4bit.model;


import com.bitcamp.project.project_4bit.entity.ClassGroup;
import com.bitcamp.project.project_4bit.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

// Signup Class의 역할
public class RegisterMember implements Serializable {

    @Min(value = 4, message = "Username is too short, must be at least 4 characters.")
    private String username;

    @Min(value = 4, message = "Password is too short, must be at least 4 characters.")
    private String password;

    @NotBlank
    @NotNull
    private String name;

    @Email
    private String email;


    // 수정된 곳
    private String phone;

    private String studentBirth;

    private ClassGroup classGroup;






    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStudentBirth() {
        return studentBirth;
    }

    public void setStudentBirth(String studentBirth) {
        this.studentBirth = studentBirth;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

//    public User user() {
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setName(name);
//        user.setEmail(email);
//        return user;
//    }

    // Service에서 사용하는 것인데, 등록할 때 멤버 등록을 위해 하나로 묶어주기 위해 만든 메서드
    // Teacher, Student의 공통된 부분을 묶어서 사용.
    public User toMember() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        return user;
    }
}
