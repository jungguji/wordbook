package com.jgji.spring.domain.user.model;

public class UserDTO {
    
    CreateUser CreateUser;
    ChangePassword ChangePassword;
    
    public CreateUser getCreateUser() {
        return new CreateUser();
    }

    public ChangePassword getChangePassword() {
        return new ChangePassword();
    }

    public static class CreateUser {
        private String id;
        private String username;
        private String password;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
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
    }
    
    
    public static class ChangePassword {
        private String userName;
        private String oldPassword;
        private String newPassword;
        private String newPasswordConfrim;
        
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getOldPassword() {
            return oldPassword;
        }
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        public String getNewPassword() {
            return newPassword;
        }
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
        public String getNewPasswordConfrim() {
            return newPasswordConfrim;
        }
        public void setNewPasswordConfrim(String newPasswordConfrim) {
            this.newPasswordConfrim = newPasswordConfrim;
        }
        
        @Override
        public String toString() {
            System.out.println("username >> " + this.userName + " old >> " + this.oldPassword);
            return "";
        }
    }
}
