package com.example.uniwares.Models;

public class Users {

    String profilepic, username, mail, pass, userId, lastMessage;

    public Users(String profilepic, String username, String mail, String pass, String userId, String lastMessage) {
        this.profilepic = profilepic;
        this.username = username;
        this.mail = mail;
        this.pass = pass;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public Users(String username, String mail, String pass) {
        this.username = username;
        this.mail = mail;
        this.pass = pass;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
