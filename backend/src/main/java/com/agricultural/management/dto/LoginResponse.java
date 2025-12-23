package com.agricultural.management.dto;

import com.agricultural.management.entity.User;

public class LoginResponse {

    private Integer code;
    private String message;
    private LoginData data;

    public LoginResponse() {
    }

    public LoginResponse(Integer code, String message, LoginData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer code;
        private String message;
        private LoginData data;

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(LoginData data) {
            this.data = data;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(code, message, data);
        }
    }

    public static class LoginData {
        private String token;
        private Long id;
        private String username;
        private String realName;
        private User.UserRole role;

        public LoginData() {
        }

        public LoginData(String token, Long id, String username, String realName, User.UserRole role) {
            this.token = token;
            this.id = id;
            this.username = username;
            this.realName = realName;
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public User.UserRole getRole() {
            return role;
        }

        public void setRole(User.UserRole role) {
            this.role = role;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String token;
            private Long id;
            private String username;
            private String realName;
            private User.UserRole role;

            public Builder token(String token) {
                this.token = token;
                return this;
            }

            public Builder id(Long id) {
                this.id = id;
                return this;
            }

            public Builder username(String username) {
                this.username = username;
                return this;
            }

            public Builder realName(String realName) {
                this.realName = realName;
                return this;
            }

            public Builder role(User.UserRole role) {
                this.role = role;
                return this;
            }

            public LoginData build() {
                return new LoginData(token, id, username, realName, role);
            }
        }
    }
}
