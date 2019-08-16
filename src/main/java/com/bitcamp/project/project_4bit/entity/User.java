package com.bitcamp.project.project_4bit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
@DynamicInsert
public class User implements UserDetails, Serializable {

    // PK : 유저_고유번호 (user_id)
    @Id
    @Column(columnDefinition = "BIGINT", name = "user_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 로그인이름 (username)
    @Column(name = "username")
    private String username;

    // 비밀번호 (password)
    @Column(name = "password")
    private String password;

    // 이름 (name)
    @Column(name = "name")
    @NotBlank
    @NotNull
    private String name;

    // 이메일 (email)
    @Column(name = "email")
    @Email
    private String email;

    // 연락처 (phone)
    @Column(name = "phone")
    private String phone;

    // 포인트합계 (point_sum)
    @Column(name = "point_sum")
    private int pointSum;

    // 유저등급 (user_level)
    @Column(name = "user_level")
    private int userLevel;

    // FK : 신분코드(role_code) from : role 테이블블
    @OneToOne
    @JoinColumn(name = "role_code")
    private Role role;

    // 유저를 등록할 때 DB에는 없는 컬럼인데, Student 테이블로 보내 주기위해 만든 변수
    @Transient
    private String studentBirth;

    // 유저를 수정할 때 DB에는 없는 컬럼인데, Student 테이블에서 classId 를 수정하기 위해 만든 변수
    @Transient
    private Long classId;



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
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

    public int getPointSum() {
        return pointSum;
    }

    public void setPointSum(int pointSum) {
        this.pointSum = pointSum;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStudentBirth() {
        return studentBirth;
    }

    public void setStudentBirth(String studentBirth) {
        this.studentBirth = studentBirth;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(role.getPrivilege());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User from(Principal principal) {
        if (principal instanceof OAuth2Authentication) {
        } else if (principal instanceof UsernamePasswordAuthenticationToken) {

        }
        return null;
    }
}

