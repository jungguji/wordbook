package com.jgji.spring.domain.user.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    
    CreateUser CreateUser;
    UserProfile ChangeProfile;
    
    public CreateUser getCreateUser() {
        return this.CreateUser;
    }

    public UserProfile getUserProfile() {
        return this.getUserProfile();
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
    
    public static class UserProfile {
        private String userName;
        private List<String> mostWrongWord = new ArrayList<String>();
        private List<BigInteger> mostWrongCount = new ArrayList<BigInteger>();
        private String oldPassword;
        private String newPassword;
        private String newPasswordConfrim;
        
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public List<String> getMostWrongWord() {
            return mostWrongWord;
        }
        public void setMostWrongWord(String mostWrongWord) {
            this.mostWrongWord.add(mostWrongWord);
        }
        public List<BigInteger> getMostWrongCount() {
            return mostWrongCount;
        }
        public void setMostWrongCount(BigInteger mostWrongCount) {
            this.mostWrongCount.add(mostWrongCount);
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
