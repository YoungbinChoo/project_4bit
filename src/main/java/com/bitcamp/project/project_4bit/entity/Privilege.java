package com.bitcamp.project.project_4bit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "privilege", uniqueConstraints = { @UniqueConstraint(columnNames = { "privilege_name" }) })
public class Privilege implements GrantedAuthority, Serializable {

    @Id
    @Column(name = "privilege_code")
    private String privilegeCode;

    @Column(name = "privilege_name")
    private String privilegeName;

    @JsonIgnore
    @ManyToMany(mappedBy = "privilege")         //DB  privilege 테이블 과 role 테이블 연결
    private Set<Role> role;

    public String getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public Set<Role> getRoles() {
        return role;
    }

    public void setRoles(Set<Role> role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return getPrivilegeName();
    }
}
