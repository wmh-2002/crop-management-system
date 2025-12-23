package com.agricultural.management.dto;

import jakarta.validation.constraints.Email;
import com.agricultural.management.entity.User;

public class UserUpdateRequest {

    private String password;

    private String realName;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String phone;

    private User.UserRole role;

    private Boolean status;

    // Getters and Setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public User.UserRole getRole() {
        return role;
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
